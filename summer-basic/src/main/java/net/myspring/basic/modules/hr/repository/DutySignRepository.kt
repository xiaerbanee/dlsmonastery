package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutySign
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutySignRepository : BaseRepository<DutySign,String>,DutySignRepositoryCustom{
    @Query("""
        SELECT
        '签到' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'QD' AS 'prefix',t1.id
        FROM
        hr_duty_sign t1 , hr_account t2 ,hr_employee t3
        WHERE
        t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
        AND t2.leader_id=?1 AND t1.status=?2 AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): List<DutyDto>
    /*fun findByFilter(dutySignQuery: DutySignQuery): List<DutySign>*/

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_sign t1
        WHERE
        t1.enabled=1
        and t1.employee_id=?1
        AND t1.duty_date >= ?2
        and t1.duty_date &lt;=?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): List<DutySign>
}
interface DutySignRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutySign>
}
class DutySignRepositoryImpl @Autowired constructor(val entityManager: EntityManager): DutySignRepositoryCustom{
    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutySign> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
        ds.employee_id,
        ds.duty_date,
        ds.duty_time
        FROM
        hr_duty_sign ds
        WHERE
        ds.duty_date gt;= :dateStart
        AND ds.duty_date lt;= :dateEnd
        and ds.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append("""
                and ds.employee_id in :accountIds
            """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DutySign::class.java)
        query.setParameter("dateStart", dateStart)
        query.setParameter("dateEnd", dateEnd)
        query.setParameter("accountIds", accountIds)
        return query.resultList as List<DutySign>
    }

}
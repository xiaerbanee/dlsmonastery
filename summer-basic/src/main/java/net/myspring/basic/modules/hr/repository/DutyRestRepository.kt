package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyRest
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyRestDto
import net.myspring.basic.modules.hr.web.query.DutyRestQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyRestRepository : BaseRepository<DutyRest, String>,DutyRestRepositoryCustom{
    @Query("""
        SELECT
        '调休' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'TX' AS 'prefix',t1.id
        FROM
        hr_duty_rest t1 , hr_account t2 ,hr_employee t3
        WHERE
        t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
        AND t2.leader_id=?1 AND t1.status=?2 AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_rest t1
        WHERE
        t1.enabled=1
        AND t1.duty_date >= ?2
        and t1.duty_date &lt;= ?3
        and t1.type = ?1
        and t1.status in ?4
    """, nativeQuery = true)
    fun findByTypeAndDutyDate(type: String, dateStart: LocalDate, dateEnd: LocalDate, statusList: MutableList<String>): MutableList<DutyRest>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_rest t1
        WHERE
        t1.enabled=1
        and t1.employee_id=?1
        and t1.duty_date >= ?2
        and t1.duty_date &lt;= ?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutyRest>
}
interface DutyRestRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyRest>

    fun findPage(pageable: Pageable, dutyRestQuery: DutyRestQuery): Page<DutyRestDto>
}
class DutyRestRepositoryImpl  @Autowired constructor(val entityManager: EntityManager): DutyRestRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyRestQuery: DutyRestQuery): Page<DutyRestDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyRest> {
        var sb = StringBuilder();
        sb.append("""
            SELECT
            dr.employee_id,
            dr.duty_date,
            dr.type,
            ov.time_start,
            ov.time_end,
            dr.status
            FROM
            hr_duty_rest dr
            WHERE
            dr.duty_date gt;= :dateStart
            AND dr.duty_date lt;= :dateEnd
            and dr.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append("""
                and dr.employee_id in :accountIds
            """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DutyRest::class.java)
        query.setParameter("dateStart", dateStart)
        query.setParameter("dateEnd", dateEnd)
        query.setParameter("accountIds", accountIds)
        return query.resultList as MutableList<DutyRest>
    }

}
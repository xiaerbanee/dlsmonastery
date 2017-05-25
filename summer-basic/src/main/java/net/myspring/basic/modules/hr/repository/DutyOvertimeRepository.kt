package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyOvertime
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
interface DutyOvertimeRepository : BaseRepository<DutyOvertime,String>,DutyOvertimeRepositoryCustom{
    @Query("""
        SELECT
        '加班' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'JB' AS 'prefix',t1.id
        FROM
        hr_duty_overtime t1 , hr_account t2 ,hr_employee t3
        WHERE
        t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
        AND t2.leader_id=?1 AND t1.status=?2 AND t1.created_date>=?3
    """, nativeQuery = true)
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): List<DutyDto>

    @Query("""
        SELECT
            t1.*
        FROM
        hr_duty_overtime t1
        WHERE
        t1.enabled=1
        and t1.employee_id=?1
        and t1.duty_date=?2
    """, nativeQuery = true)
    fun findByDutyDate(employeeId: String, dutyDate: LocalDate): List<DutyOvertime>

    @Query("""
        SELECT
            t1.*
        FROM
        hr_duty_overtime t1
        WHERE
        t1.enabled=1
        and t1.status=?4
        and t1.employee_id=?1
        and t1.duty_date>=?2
        and t1.duty_date &lt;=?3
    """, nativeQuery = true)
    fun findByDutyDateAndStatus(employeeId: String, dutyDateStart: LocalDate, dutyDateEnd: LocalDate, status: String): List<DutyOvertime>

    @Query("""
        SELECT
          t1.*
        FROM
          hr_duty_overtime t1
          WHERE
          t1.enabled=1
          and t1.status=?4
          and t1.duty_date>=?2
          and t1.duty_date &lt;=?3
          and t1.employee_id=?1
    """, nativeQuery = true)
    fun findByIdAndDate(employeeId: String, dateStart: LocalDateTime, dateEnd: LocalDateTime, status: String): List<DutyOvertime>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_overtime t1
        WHERE
        t1.enabled=1
        AND t1.duty_date >= ?1 and t1.duty_date &lt;= ?2
        and t1.status in ?3
    """, nativeQuery = true)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: List<String>): List<DutyOvertime>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_duty_overtime t1
        WHERE
        t1.enabled=1
        and t1.employee_id=?1
        and t1.duty_date >= ?2
        and t1.duty_date &lt;= ?3
    """, nativeQuery = true)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): List<DutyOvertime>


}
interface DutyOvertimeRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutyOvertime>
}
class DutyOvertimeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DutyOvertimeRepositoryCustom{
    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: List<Long>): List<DutyOvertime> {
        var sb = StringBuffer()
        sb.append("""
            SELECT
            ov.employee_id,
            ov.duty_date,
            ov.time_start,
            ov.time_end
            FROM
            hr_duty_overtime ov
            WHERE
            ov.duty_date gt;= :dateStart
            AND ov.duty_date lt;= :dateEnd
            and ov.enabled=1
        """)
        if (CollectionUtil.isNotEmpty(accountIds)) {
            sb.append(" and ov.employee_id in :accountIds")
        }
        var query = entityManager.createNativeQuery(sb.toString(), DutyOvertime::class.java)
        query.setParameter("dateStart", dateStart)
        query.setParameter("dateEnd", dateEnd)
        query.setParameter("accountIds", accountIds)
        return query.resultList as List<DutyOvertime>
    }

}
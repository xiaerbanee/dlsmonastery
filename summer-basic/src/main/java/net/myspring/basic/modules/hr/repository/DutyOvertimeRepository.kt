package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyOvertime
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto
import net.myspring.basic.modules.hr.web.query.DutyOvertimeQuery
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyOvertimeRepository : BaseRepository<DutyOvertime,String>,DutyOvertimeRepositoryCustom{
    @Query("""
        SELECT
            t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId=?1
        and t1.dutyDate=?2
    """)
    fun findByDutyDate(employeeId: String, dutyDate: LocalDate): MutableList<DutyOvertime>

    @Query("""
        SELECT
            t1
        FROM  #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.status=?4
        and t1.employeeId=?1
        and t1.dutyDate>=?2
        and t1.dutyDate <=?3
    """)
    fun findByDutyDateAndStatus(employeeId: String, dutyDateStart: LocalDate, dutyDateEnd: LocalDate, status: String): MutableList<DutyOvertime>

    @Query("""
        SELECT
          t1
          FROM  #{#entityName} t1
          WHERE
          t1.enabled=1
          and t1.status=?4
          and t1.dutyDate>=?2
          and t1.dutyDate <=?3
          and t1.employeeId=?1
    """)
    fun findByIdAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate, status: String): MutableList<DutyOvertime>

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        AND t1.dutyDate >= ?1 and t1.dutyDate <= ?2
        and t1.status in ?3
    """)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: MutableList<String>): MutableList<DutyOvertime>

    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId=?1
        and t1.dutyDate >= ?2
        and t1.dutyDate <= ?3
    """)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutyOvertime>


}
interface DutyOvertimeRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyOvertime>

    fun findPage(pageable: Pageable, dutyOvertimeQuery: DutyOvertimeQuery): Page<DutyOvertimeDto>

    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>
}
class DutyOvertimeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DutyOvertimeRepositoryCustom{
    override fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("leaderId", leaderId)
        paramMap.put("status", status)
        paramMap.put("dateStart", dateStart)
        return namedParameterJdbcTemplate.query("""
            SELECT
            '加班' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'JB' AS 'prefix',t1.id
            FROM
            hr_duty_overtime t1 , hr_account t2 ,hr_employee t3
            WHERE
            t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
            AND t2.leader_id=:leaderId AND t1.status=:status AND t1.created_date>=:dateStart
        """, paramMap, BeanPropertyRowMapper(DutyDto::class.java))
    }

    override fun findPage(pageable: Pageable, dutyOvertimeQuery: DutyOvertimeQuery): Page<DutyOvertimeDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyOvertime> {
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
            sb.append(" and ov.employee_id in (:accountIds)")
        }
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateStart)
        paramMap.put("accountIds", dateStart)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(DutyOvertime::class.java))
    }

}
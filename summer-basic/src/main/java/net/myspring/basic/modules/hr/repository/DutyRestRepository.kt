package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyRest
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyRestDto
import net.myspring.basic.modules.hr.web.query.DutyRestQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyRestRepository : BaseRepository<DutyRest, String>,DutyRestRepositoryCustom{
    @Query("""
        SELECT
        t1
        FROM #{#entityName} t1
        WHERE
        t1.enabled=1
        AND t1.dutyDate >= ?2
        and t1.dutyDate <= ?3
        and t1.type = ?1
        and t1.status in ?4
    """)
    fun findByTypeAndDutyDate(type: String, dateStart: LocalDate, dateEnd: LocalDate, statusList: MutableList<String>): MutableList<DutyRest>

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
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutyRest>
}
interface DutyRestRepositoryCustom{
    fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyRest>

    fun findPage(pageable: Pageable, dutyRestQuery: DutyRestQuery): Page<DutyRestDto>

    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>
}
class DutyRestRepositoryImpl  @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyRestRepositoryCustom{
    override fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("leaderId", leaderId)
        paramMap.put("status", status)
        paramMap.put("dateStart", dateStart)
        return namedParameterJdbcTemplate.query("""
            SELECT
            '调休' as dutyType,t1.duty_date,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'TX' AS 'prefix',t1.id
            FROM
            hr_duty_rest t1 , hr_account t2 ,hr_employee t3
            WHERE
            t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
            AND t2.leader_id=:leaderId AND t1.status=:status AND t1.created_date>=:dateStart
        """, paramMap, BeanPropertyRowMapper(DutyDto::class.java))
    }

    override fun findPage(pageable: Pageable, dutyRestQuery: DutyRestQuery): Page<DutyRestDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
              t1.*
            FROM
            hr_duty_rest t1,
            hr_account account,
            sys_office office
            WHERE
            t1.created_by=account.id
            and account.office_id=office.id
            and t1.enabled=1
        """)
        if (dutyRestQuery.createdBy != null) {
            sb.append(" AND t1.created_by=:createdBy ")
        }
        if (dutyRestQuery.dutyDateStart != null) {
            sb.append(" AND t1.duty_date>= :dutyDateStart ")
        }
        if (dutyRestQuery.dutyDateEnd != null) {
            sb.append(" AND t1.duty_date < :dutyDateEnd ")
        }
        if (dutyRestQuery.type != null) {
            sb.append(" AND t1.type=:type ")
        }
        if (dutyRestQuery.dateType != null) {
            sb.append(" AND t1.date_type=:dateType ")
        }
        if (dutyRestQuery.officeIds != null && dutyRestQuery.officeIds.size != 0) {
            sb.append(" and office.id IN :officeIds ")
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(dutyRestQuery),BeanPropertyRowMapper(DutyRestDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(dutyRestQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

    override fun findByAccountIdAndDutyDate(dateStart: LocalDate, dateEnd: LocalDate, accountIds: MutableList<Long>): MutableList<DutyRest> {
        var sb = StringBuilder()
        var paramMap = HashMap<String, Any>()
        paramMap.put("dateStart", dateStart)
        paramMap.put("dateEnd", dateEnd)
        paramMap.put("accountIds", accountIds)
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
                and dr.employee_id in (:accountIds)
            """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(DutyRest::class.java))
    }

}
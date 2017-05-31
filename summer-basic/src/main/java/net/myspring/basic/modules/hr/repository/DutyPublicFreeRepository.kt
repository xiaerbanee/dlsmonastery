package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyPublicFree
import net.myspring.basic.modules.hr.dto.DutyDto
import net.myspring.basic.modules.hr.dto.DutyPublicFreeDto
import net.myspring.basic.modules.hr.web.query.DutyPublicFreeQuery
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
interface DutyPublicFreeRepository : BaseRepository<DutyPublicFree,String>,DutyPublicFreeRepositoryCustom{
    @Query("""
        SELECT
        t1
        FROM
        #{#entityName} t1
        WHERE
        t1.enabled=1
        AND t1.freeDate >= ?1
        and t1.freeDate <= ?2
        and t1.status in ?3
    """)
    fun findByDateAndStatusList(dateStart: LocalDate, dateEnd: LocalDate, statusList: MutableList<String>): MutableList<DutyPublicFree>

    @Query("""
        SELECT
        t1
        FROM
        #{#entityName} t1
        WHERE
        t1.enabled=1
        and t1.employeeId=?1
        and t1.freeDate >= ?2
        and t1.freeDate <= ?3
    """)
    fun findByEmployeeAndDate(employeeId: String, dateStart: LocalDate, dateEnd: LocalDate): MutableList<DutyPublicFree>
}

interface DutyPublicFreeRepositoryCustom{
    fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto>

    fun findPage(pageable: Pageable, dutyPublicFreeQuery: DutyPublicFreeQuery): Page<DutyPublicFreeDto>
}

class DutyPublicFreeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyPublicFreeRepositoryCustom{
    override fun findByAuditable(leaderId: String, status: String, dateStart: LocalDateTime): MutableList<DutyDto> {
        var paramMap = HashMap<String, Any>()
        paramMap.put("leaderId", leaderId)
        paramMap.put("status", status)
        paramMap.put("dateStart", dateStart)
        return namedParameterJdbcTemplate.query("""
            SELECT
            '公休' as dutyType,t1.free_date AS dutyDate,t1.remarks,t2.login_name as 'account.loginName',t2.leader_id AS 'account.leaderId' ,'GX' AS 'prefix',t1.id
            FROM
            hr_duty_public_free t1 , hr_account t2 ,hr_employee t3
            WHERE
            t1.enabled=1 AND t1.employee_id=t3.id and t3.account_id=t2.id
            AND t2.leader_id=:leaderId AND t1.status=:status AND t1.created_date>=:dateStart
        """, paramMap, BeanPropertyRowMapper(DutyDto::class.java))
    }

    override fun findPage(pageable: Pageable, dutyPublicFreeQuery: DutyPublicFreeQuery): Page<DutyPublicFreeDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            hr_duty_public_free t1,
            hr_account account,
            sys_office office
            WHERE
            t1.created_by=account.id
            and account.office_id=office.id
            and t1.enabled=1
        """)
        if (dutyPublicFreeQuery.dutyDateStart != null) {
            sb.append(" AND t1.free_date > :dutyDateStart ")
        }
        if (dutyPublicFreeQuery.dutyDateEnd != null) {
            sb.append(" AND t1.free_date < :dutyDateEnd ")
        }
        if (dutyPublicFreeQuery.employeeName != null) {
            sb.append("""
                and t1.employee_id in (
                select t2.id
                from hr_employee t2
                where t2.name LIKE CONCAT('%',:employeeName,'%')
                )
            """)
        }
        if (dutyPublicFreeQuery.officeIds != null && dutyPublicFreeQuery.officeIds.size != 0) {
            sb.append(" and office.id IN :officeIds ")
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(dutyPublicFreeQuery),BeanPropertyRowMapper(DutyPublicFreeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(dutyPublicFreeQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}
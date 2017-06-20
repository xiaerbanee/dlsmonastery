package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyAnnual
import net.myspring.basic.modules.hr.dto.DutyAnnualDto
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery
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

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyAnnualRepository : BaseRepository<DutyAnnual,String>,DutyAnnualRepositoryCustom{
    @Query("""
        select t
        FROM  #{#entityName} t
        where t.employeeId=?1
        order by annualYear desc
    """)
    fun findByEmployee(employeeId: String): List<DutyAnnual>
}
interface DutyAnnualRepositoryCustom{
    fun findPage(pageable: Pageable, dutyAnnualQuery: DutyAnnualQuery): Page<DutyAnnualDto>
}
class DutyAnnualRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DutyAnnualRepositoryCustom{
    override fun findPage(pageable: Pageable, dutyAnnualQuery: DutyAnnualQuery): Page<DutyAnnualDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT t1.*,t3.office_id,t3.position_id
            FROM  hr_duty_annual t1,hr_employee t2,hr_account t3
            where t1.enabled=1
            and t1.employee_id=t2.id
            and t2.account_id=t3.id
        """)
        if (dutyAnnualQuery.name != null) {
            sb.append("""
            and t1.employee_id in(
                SELECT id
                FROM  hr_employee
                where enabled=1
                and name like concat('%',:name,'%')
            )
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(dutyAnnualQuery), BeanPropertyRowMapper(DutyAnnualDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(dutyAnnualQuery),Long::class.java);
        return PageImpl(list,pageable,count);

    }

}
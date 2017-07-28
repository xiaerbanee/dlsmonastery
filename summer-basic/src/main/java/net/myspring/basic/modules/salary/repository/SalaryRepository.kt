package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Position
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.web.query.PositionQuery
import net.myspring.basic.modules.salary.domain.Salary
import net.myspring.basic.modules.salary.dto.SalaryDto
import net.myspring.basic.modules.salary.web.query.SalaryQuery
import net.myspring.basic.modules.sys.dto.RoleDto
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/25.
 */
interface SalaryRepository : BaseRepository<Salary,String>,SalaryRepositoryCustom{

    @Modifying
    @Query("delete from Salary where salaryTemplateId=?1 and employeeId in (?2) and month=?3")
    fun deleteBySalaryTemplateId(salaryTemplateId: String, employeeIds: List<String>, month: String)
}
interface SalaryRepositoryCustom{
    fun findPage(pageable: Pageable, salaryQuery: SalaryQuery): Page<SalaryDto>
}
class SalaryRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): SalaryRepositoryCustom{
    override fun findPage(pageable: Pageable, salaryQuery: SalaryQuery): Page<SalaryDto> {
        var sb = StringBuilder("""select t1.* ,employee.name as employeeName,office.name as officeName
        from hr_salary t1,hr_employee employee,hr_account account ,sys_office office
        where t1.enabled=1
        and t1.employee_id=employee.id
        and employee.account_id=account.id
        and account.office_id=office.id
        and ( employee.id=:employeeId or t1.created_by=:accountId)
""");
        if (StringUtils.isNotBlank(salaryQuery.projectName)) {
            sb.append(" and t1.project_name like concat('%',:projectName,'%')");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(salaryQuery), BeanPropertyRowMapper(SalaryDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(salaryQuery), Long::class.java);
        return PageImpl(list, pageable, count);
    }
}
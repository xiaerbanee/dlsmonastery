package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Position
import net.myspring.basic.modules.hr.dto.PositionDto
import net.myspring.basic.modules.hr.web.query.PositionQuery
import net.myspring.basic.modules.salary.domain.SalaryTemplate
import net.myspring.basic.modules.salary.dto.SalaryDto
import net.myspring.basic.modules.salary.dto.SalaryTemplateDto
import net.myspring.basic.modules.salary.web.query.SalaryQuery
import net.myspring.basic.modules.salary.web.query.SalaryTemplateQuery
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
interface SalaryTemplateRepository : BaseRepository<SalaryTemplate, String>, SalaryTemplateRepositoryCustom {

}

interface SalaryTemplateRepositoryCustom {
    fun findPage(pageable: Pageable, salaryTemplateQuery: SalaryTemplateQuery): Page<SalaryTemplateDto>
}

class SalaryTemplateRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : SalaryTemplateRepositoryCustom {
    override fun findPage(pageable: Pageable, salaryTemplateQuery: SalaryTemplateQuery): Page<SalaryTemplateDto> {
        var sb = StringBuilder("""
            select t1.*,GROUP_CONCAT(DISTINCT t2.name) as salaryTemplateDetails
            from hr_salary_template t1,hr_salary_template_detail t2
            where t2.salary_template_id=t1.id
            and t1.enabled=1
            and t2.enabled=1
            group by t1.id
        """);
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(salaryTemplateQuery), BeanPropertyRowMapper(SalaryTemplateDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(salaryTemplateQuery), Long::class.java);
        return PageImpl(list, pageable, count);
    }
}
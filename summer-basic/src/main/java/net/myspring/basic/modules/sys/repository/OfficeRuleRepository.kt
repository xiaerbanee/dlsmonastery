package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Office
import net.myspring.basic.modules.sys.domain.OfficeRule
import net.myspring.basic.modules.sys.dto.OfficeDto
import net.myspring.basic.modules.sys.dto.OfficeRuleDto
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.web.query.OfficeQuery
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery
import net.myspring.basic.modules.sys.web.query.PermissionQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */

@CacheConfig(cacheNames = arrayOf("officeRules"))
interface OfficeRuleRepository  : BaseRepository<OfficeRule, String> ,OfficeRuleRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): OfficeRule

    @CachePut(key="#p0.id")
    fun save(officeRule: OfficeRule): OfficeRule

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.name=?1
     """)
    fun findByName(name: String): OfficeRule

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        order BY  t.level asc
     """)
    fun findTopOfficeRule(pageable: Pageable): Page<OfficeRule>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.level > (
          SELECT level FROM OfficeRule where id=?1
        )
     """)
    fun findNextOfficeRule(id: String): List<OfficeRule>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
     """)
    fun findAllEnabled():MutableList<OfficeRule>
}


interface OfficeRuleRepositoryCustom{

    fun findPage(pageable: Pageable, officeRuleQuery: OfficeRuleQuery): Page<OfficeRuleDto>?
}

class OfficeRuleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): OfficeRuleRepositoryCustom{
    override fun findPage(pageable: Pageable, officeRuleQuery: OfficeRuleQuery): Page<OfficeRuleDto>? {
        var sb = StringBuilder("select * from sys_office_rule where enabled=1 ");
        if(StringUtils.isNotBlank(officeRuleQuery.name)) {
            sb.append(" and name like concat('%',:name,'%')");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(officeRuleQuery), BeanPropertyRowMapper(OfficeRuleDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(officeRuleQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }


}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.enums.ExpressCompanyTypeEnum
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ExpressCompany
import net.myspring.future.modules.basic.dto.ExpressCompanyDto
import net.myspring.future.modules.basic.web.query.ExpressCompanyQuery
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
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("expressCompanies"))
interface ExpressCompanyRepository : BaseRepository<ExpressCompany,String>,ExpressCompanyRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): ExpressCompany

    override fun findAll(): MutableList<ExpressCompany>

    @CachePut(key = "#p0.id")
    fun save(expressCompany: ExpressCompany): ExpressCompany

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<ExpressCompany>

    @Query("""
        SELECT t1.*
        FROM crm_express_company t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<ExpressCompany>

    fun findByExpressType(expressType: String): MutableList<ExpressCompany>

    fun findByNameContaining(name: String): MutableList<ExpressCompany>

}

interface ExpressCompanyRepositoryCustom{
    fun findPage(pageable: Pageable, expressCompanyQuery: ExpressCompanyQuery): Page<ExpressCompanyDto>


    fun findByNameLike(name: String): MutableList<ExpressCompanyDto>
}

class ExpressCompanyRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ExpressCompanyRepositoryCustom{
    override fun findByNameLike(name: String): MutableList<ExpressCompanyDto> {
        val params = HashMap<String, Any>()
        params.put("name", name)
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.*
            FROM
                crm_express_company t1
            WHERE
                t1.enabled=1
                AND t1.name LIKE CONCAT('%',:name,'%')
          """, params, BeanPropertyRowMapper(ExpressCompanyDto::class.java))
    }

    override fun findPage(pageable: Pageable, expressCompanyQuery: ExpressCompanyQuery): Page<ExpressCompanyDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_express_company t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(expressCompanyQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(expressCompanyQuery.expressType)&&expressCompanyQuery.expressType!=ExpressCompanyTypeEnum.所有.name) {
            sb.append("""  and t1.express_type LIKE CONCAT('%',:expressType,'%') """)
        }
        if (StringUtils.isNotEmpty(expressCompanyQuery.reachPlace)) {
            sb.append("""  and t1.reach_place LIKE CONCAT('%',:reachPlace,'%') """)
        }
        if (StringUtils.isNotEmpty(expressCompanyQuery.mobilePhone)) {
            sb.append("""  and t1.mobile_phone LIKE CONCAT('%',:mobilePhone,'%') """)
        }
        if (StringUtils.isNotEmpty(expressCompanyQuery.contator)) {
            sb.append("""  and t1.contator LIKE CONCAT('%',:contator,'%') """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(expressCompanyQuery), BeanPropertyRowMapper(ExpressCompanyDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(expressCompanyQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
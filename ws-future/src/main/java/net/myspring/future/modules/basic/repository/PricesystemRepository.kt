package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Pricesystem
import net.myspring.future.modules.basic.dto.PricesystemDto
import net.myspring.future.modules.basic.web.query.PricesystemQuery
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
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("pricesystems"))
interface PricesystemRepository : BaseRepository<Pricesystem,String>,PricesystemRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Pricesystem

    override fun findAll(): MutableList<Pricesystem>

    @CachePut(key = "#p0.id")
    fun save(pricesystem: Pricesystem): Pricesystem

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
    """)
    fun findAllEnabled(): MutableList<Pricesystem>


    fun findByName(name:String):Pricesystem

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.id in :ids
    """)
    fun findByIds(@Param("ids")ids: MutableList<String>): MutableList<Pricesystem>
}

interface PricesystemRepositoryCustom{
    fun findPage(pageable: Pageable, pricesystemQuery: PricesystemQuery): Page<PricesystemDto>
}

class PricesystemRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):PricesystemRepositoryCustom{

    override fun findPage(pageable: Pageable, pricesystemQuery: PricesystemQuery): Page<PricesystemDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_pricesystem t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(pricesystemQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(pricesystemQuery), BeanPropertyRowMapper(PricesystemDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(pricesystemQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Chain
import net.myspring.future.modules.basic.dto.ChainDto
import net.myspring.future.modules.basic.web.query.ChainQuery
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
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("chains"))
interface ChainRepository : BaseRepository<Chain,String>,ChainRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Chain

    override fun findAll(): MutableList<Chain>

    @CachePut(key = "#p0.id")
    fun save(chain: Chain): Chain

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
    """)
    fun findAllEnabled(): MutableList<Chain>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        WHERE t1.id IN ?1
    """)
    fun findByIds(ids: MutableList<String>): MutableList<Chain>

}

interface ChainRepositoryCustom{
    fun findDepotId(id: String): MutableList<String>

    fun findPage(pageable: Pageable, chainQuery: ChainQuery): Page<ChainDto>
}

class ChainRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ChainRepositoryCustom{

    override fun findDepotId(id: String): MutableList<String>{
        return namedParameterJdbcTemplate.queryForList("""
            select id from crm_depot where chain_id=:id
    """,Collections.singletonMap("id",id),String::class.java)
    }

    override fun findPage(pageable: Pageable, chainQuery: ChainQuery): Page<ChainDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_chain t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(chainQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(chainQuery), BeanPropertyRowMapper(ChainDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(chainQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}


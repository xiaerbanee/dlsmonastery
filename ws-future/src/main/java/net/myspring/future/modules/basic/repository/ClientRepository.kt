package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Client
import net.myspring.future.modules.basic.dto.ClientDto
import net.myspring.future.modules.basic.web.query.ClientQuery
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
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("client"))
interface ClientRepository :BaseRepository<Client,String>,ClientRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Client

    override fun findAll(): MutableList<Client>

    @CachePut(key = "#p0.id")
    fun save(client: Client): Client

    @Query("""
        SELECT t1.*
        FROM crm_client t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Client>

    @Query("""
        select t1.*
        from crm_client t1, crm_depot t2
        where
        t1.enabled=1
        and t1.id = t2.client_id
        and t2.id = ?1
    """, nativeQuery = true)
    fun findByDepotId(depotId: String): Client

    fun findByNameContaining(name: String): MutableList<Client>
}

interface ClientRepositoryCustom{
    fun findPage(pageable: Pageable, clientQuery: ClientQuery): Page<ClientDto>
}

class ClientRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ClientRepositoryCustom{

    override fun findPage(pageable: Pageable, clientQuery: ClientQuery): Page<ClientDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_client t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(clientQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(clientQuery), BeanPropertyRowMapper(ClientDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(clientQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
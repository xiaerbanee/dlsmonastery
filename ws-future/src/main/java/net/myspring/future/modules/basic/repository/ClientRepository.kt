package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Client
import net.myspring.future.modules.basic.dto.ClientDto
import net.myspring.future.modules.basic.dto.ReceivableDto
import net.myspring.future.modules.basic.web.query.ClientQuery
import net.myspring.future.modules.basic.web.query.ReceivableQuery
import net.myspring.util.collection.CollectionUtil
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
import java.time.LocalDateTime
import java.util.*


/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("clients"))
interface ClientRepository :BaseRepository<Client,String>,ClientRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Client

    override fun findAll(): MutableList<Client>

    @CachePut(key = "#p0.id")
    fun save(client: Client): Client

    fun findByOutIdIn(outIdList:MutableList<String>):MutableList<Client>

    @Query("""
        SELECT t1.*
        FROM crm_client t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Client>

    fun findByNameContaining(name: String): MutableList<Client>

    @Query("""
        SELECT
        Max(t1.outDate)
        FROM #{#entityName} t1
        WHERE  t1.enabled = 1
    """)
    fun findMaxOutDate(): LocalDateTime
}

interface ClientRepositoryCustom{
    fun findPage(pageable: Pageable, clientQuery: ClientQuery): Page<ClientDto>

    fun findByDepotId(depotId: String): Client?

    fun findReceivableList(pageable: Pageable, receivableQuery: ReceivableQuery): Page<ReceivableDto>

}

class ClientRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ClientRepositoryCustom{
    override fun findReceivableList(pageable: Pageable, receivableQuery: ReceivableQuery): Page<ReceivableDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT
            t1.id,
            t1.name,
            t1.out_id,
            group_concat(DISTINCT ''+t2.office_id) depotOfficeIds,
            group_concat(DISTINCT ''+t2.area_id) depotAreaIds
        FROM
            crm_client t1, crm_depot t2

        WHERE
            t1.enabled =1 AND t2.client_id = t1.id AND t2.is_hidden = 0 AND t2.enabled = 1

        """)

        if(receivableQuery.accountTaxPermitted != null && receivableQuery.accountTaxPermitted){
            sb.append("""
                and t2.tax_name IS NOT NULL
                and t2.tax_name != ''
            """)
        }
        if(StringUtils.isNotBlank(receivableQuery.clientName)){
            sb.append("""  and t1.name like concat('%', :clientName,'%') """)
        }
        if(CollectionUtil.isNotEmpty(receivableQuery.officeIds)){
            sb.append("""  and t2.office_id in (:officeIds)  """)
        }
        if(StringUtils.isNotBlank(receivableQuery.areaId)){
            sb.append("""  and t2.area_id = :areaId  """)
        }
        if(CollectionUtil.isNotEmpty(receivableQuery.depotIdList)){
            sb.append("""  and t2.id in (:depotIdList) """)
        }
        if(CollectionUtil.isNotEmpty(receivableQuery.officeIdList)){
            sb.append("""  and t2.office_id in (:officeIdList) """)
        }

        sb.append("""  group by t1.id  """)

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(receivableQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ReceivableDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }

    override fun findByDepotId(depotId: String): Client? {
        val resultList = namedParameterJdbcTemplate.query("""
        select t1.*
        from crm_client t1, crm_depot t2
        where
        t1.id = t2.client_id
        and t2.id = :depotId
        """, Collections.singletonMap("depotId",depotId),BeanPropertyRowMapper(Client::class.java))

        if(resultList.size>0){
            return resultList[0]
        }else{
            return null
        }
    }

    override fun findPage(pageable: Pageable, clientQuery: ClientQuery): Page<ClientDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*,GROUP_CONCAT(DISTINCT t2.name) as depotNameStr
            FROM
                crm_client t1,crm_depot t2
            WHERE
                t1.enabled=1
                and t2.enabled=1
                and t2.client_id=t1.id
        """)
        if (StringUtils.isNotEmpty(clientQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        sb.append("""  group by t1.id """)
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(clientQuery), BeanPropertyRowMapper(ClientDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(clientQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
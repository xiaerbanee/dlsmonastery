package net.myspring.future.modules.basic.repository


import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystem
import net.myspring.future.modules.basic.dto.AdPricesystemDto
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("adPricesystems"))
interface AdpricesystemRepository : BaseRepository<AdPricesystem,String>,AdpricesystemRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): AdPricesystem

    override fun findAll(): MutableList<AdPricesystem>

    @CachePut(key = "#p0.id")
    fun save(adPricesystem: AdPricesystem): Int


    fun findByEnabledIsTrue(): MutableList<AdPricesystem>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        WHERE t1.enabled = 1
    """)
    fun findList(adPricesystemQuery: AdPricesystemQuery): MutableList<AdPricesystem>

    fun findByName(name: String): AdPricesystem


//    fun saveOfficeIds(@Param("adPricesystemId")adPricesystemId: String,@Param("officeIds")officeIds: MutableList<String>): Int

    @Query("""
       SELECT t1.office_id
        FROM crm_ad_pricesystem_office t1
        where t1.ad_pricesystem_id=?1
    """, nativeQuery = true)
    fun findOfficeById(id: String): MutableList<String>
}

interface AdpricesystemRepositoryCustom{

    fun deleteOfficeIds(id: String):Int

    fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto>
}

class AdpricesystemRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdpricesystemRepositoryCustom{

    override fun deleteOfficeIds(id: String):Int{
        return namedParameterJdbcTemplate.queryForObject("""
             DELETE FROM crm_ad_pricesystem_office where ad_pricesystem_id = :id
        """,Collections.singletonMap("id",id),Int::class.java)
    }

    override fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto> {
        TODO("findPage")
        /*val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(adPricesystemQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), AdPricesystemDto::class.java)

        return query.resultList as Page<AdPricesystemDto>*/
    }
}


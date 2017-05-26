package net.myspring.future.modules.basic.repository


import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystem
import net.myspring.future.modules.basic.dto.AdPricesystemDto
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("adPricesystems"))
interface AdpricesystemRepository : BaseRepository<AdPricesystem,String>,AdpricesystemRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): AdPricesystem

    override fun findAll(): MutableList<AdPricesystem>

    @CachePut(key = "#id")
    fun save(adPricesystem: AdPricesystem): Int

    @Query("""
        SELECT t1.*
        FROM crm_ad_price_system t1
        WHERE t1.enabled = 1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<AdPricesystem>

    @Query("""
        SELECT t1.*
        FROM crm_ad_price_system t1
        WHERE t1.enabled = 1
    """, nativeQuery = true)
    fun findList(adPricesystemQuery: AdPricesystemQuery): MutableList<AdPricesystem>

    fun findByName(name: String): AdPricesystem

    @Query("""
       DELETE FROM crm_ad_pricesystem_office where ad_pricesystem_id = ?1
    """, nativeQuery = true)
    fun deleteOfficeIds(id: String): Int

//    fun saveOfficeIds(@Param("adPricesystemId")adPricesystemId: String,@Param("officeIds")officeIds: MutableList<String>): Int

    @Query("""
       SELECT t1.office_id
        FROM crm_ad_pricesystem_office t1
        where t1.ad_pricesystem_id=?1
    """, nativeQuery = true)
    fun findOfficeById(id: String): MutableList<String>
}

interface AdpricesystemRepositoryCustom{
    fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto>
}

class AdpricesystemRepositoryImpl @Autowired constructor(val entityManager: EntityManager):AdpricesystemRepositoryCustom{

    override fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), AdPricesystemDto::class.java)

        return query.resultList as Page<AdPricesystemDto>
    }
}


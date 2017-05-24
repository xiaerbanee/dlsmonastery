package net.myspring.future.modules.basic.repository


import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystem
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("adPricesystems"))
interface AdpricesystemRepository : BaseRepository<AdPricesystem,String> {

    @Cacheable
    override fun findOne(id: String): AdPricesystem

    override fun findAll(): List<AdPricesystem>

    @CachePut(key = "#id")
    fun save(adPricesystem: AdPricesystem): Int

    @Query("""
        SELECT t1.*
        FROM crm_ad_price_system t1
        WHERE t1.enabled = 1
    """, nativeQuery = true)
    fun findAllEnabled(): List<AdPricesystem>

    @Query("""
        SELECT t1.*
        FROM crm_ad_price_system t1
        WHERE t1.enabled = 1
    """, nativeQuery = true)
    fun findList(adPricesystemQuery: AdPricesystemQuery): List<AdPricesystem>

    fun findByName(name: String): AdPricesystem

    @Query("""
       DELETE FROM crm_ad_pricesystem_office where ad_pricesystem_id = ?1
    """, nativeQuery = true)
    fun deleteOfficeIds(id: String): Int

    fun saveOfficeIds(@Param("adPricesystemId")adPricesystemId: String,@Param("officeIds")officeIds: List<String>): Int

    @Query("""
       SELECT t1.office_id
        FROM crm_ad_pricesystem_office t1
        where t1.ad_pricesystem_id=?1
    """, nativeQuery = true)
    fun findOfficeById(id: String): List<String>
}
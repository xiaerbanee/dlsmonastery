package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.ProductTypeDto
import net.myspring.future.modules.basic.web.query.ProductTypeQuery
import org.apache.ibatis.annotations.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("productTypes"))
interface ProductTypeRepository : BaseRepository<ProductType,String> {

    @Cacheable
    override fun findOne(id: String): ProductType

    override fun findAll(): List<ProductType>

    @CachePut(key = "#id")
    fun save(productType: ProductType): Int

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<ProductType>

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<ProductType>

    fun findList(@Param("p") map: Map<String, Any>): List<ProductType>

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
        and t1.demo_phone_type_id in ?1
    """, nativeQuery = true)
    fun findByDemoPhoneTypeIds(dempProductTypeIds: List<String>): List<ProductType>

    fun findByNameLike(@Param("name") name: String): List<ProductType>

    fun updateDemoPhoneType(@Param("demoPhoneTypeId") demoPhoneTypeId: String, @Param("list") ids: List<String>): Int

    fun updateDemoPhoneTypeToNull(demoPhoneTypeId: String): Int

    @Query("""
        SELECT t1.* from crm_product_type t1
        where t1.score_type=1
        and t1.enabled = 1
        and t1.price1 is not null order by t1.name
    """, nativeQuery = true)
    fun findAllScoreType(): List<ProductType>

    fun findByDemoPhoneTypeId(demoPhoneTypeId: String): List<ProductType>
}
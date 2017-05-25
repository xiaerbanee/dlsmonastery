package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Product
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.ProductDto
import net.myspring.future.modules.basic.web.query.ProductQuery
import org.apache.ibatis.annotations.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("products"))
interface ProductRepository : BaseRepository<Product,String> {
    @Cacheable
    override fun findOne(id: String): Product

    override fun findAll(): List<Product>

    @CachePut(key = "#id")
    fun save(product: Product): Int

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Product>


    fun findHasImeProduct(): List<Product>

    fun findByNameLike(name: String): List<Product>

    fun findByCodeLike(code: String): List<Product>

    fun findByNameLikeHasIme(name: String): List<Product>

    fun findByCodeLikeHasIme(code: String): List<Product>

    fun findByName(name: String): Product

    fun findByOutId(outId: String): Product

    fun findFilter(@Param("p") productQuery: ProductQuery): List<Product>

    fun findByOutName(): List<ProductDto>

    fun findByOutGroupIdIn(outGroupIds: List<String>): List<Product>

    fun findByProductTypeId(productTypeId: String): List<Product>

    fun updateProductTypeId(@Param("productTypeId") id: String, @Param("list") ids: List<String>): Int

    fun updateProductTypeToNull(productTypeId: String): Int

    fun findByOutGroupIdsAndAllowOrder(@Param("outGroupIds") outGroupIds: List<String>, @Param("allowOrder") allowOrder: Boolean): List<ProductDto>

    @Query("""
        select
        MAX(out_date)
        from
        crm_product t1
        where
        t1.enabled=1
    """, nativeQuery = true)
    fun getMaxOutDate(): LocalDateTime

    @Query("""
        SELECT  t1.*  from crm_product_type t1 where
        t1.enabled=1
    """, nativeQuery = true)
    fun findProductTypeList(): List<ProductType>

    fun findIntersectionOfBothPricesystem(@Param("pricesystemId1") pricesystemId1: String, @Param("pricesystemId2") pricesystemId2: String): List<ProductDto>

    fun findByNameList(nameList: List<String>): List<Product>
}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Product
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.ProductDto
import net.myspring.future.modules.basic.web.query.ProductQuery
import org.apache.ibatis.annotations.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("products"))
interface ProductRepository : BaseRepository<Product,String>,ProductRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Product

    override fun findAll(): MutableList<Product>

    @CachePut(key = "#id")
    fun save(product: Product): Int

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Product>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
        AND t1.has_ime = 1
    """, nativeQuery = true)
    fun findHasImeProduct(): MutableList<Product>

    fun findByNameLike(name: String): MutableList<Product>

    fun findByCodeLike(code: String): MutableList<Product>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
        AND t1.has_ime = 1
        AND t1.name LIKE concat( '%', ?1,'%')
    """, nativeQuery = true)
    fun findByNameLikeHasIme(name: String): MutableList<Product>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
        AND t1.has_ime = 1
        AND t1.code LIKE concat( '%', ?1,'%')
    """, nativeQuery = true)
    fun findByCodeLikeHasIme(code: String): MutableList<Product>

    fun findByName(name: String): Product

    fun findByOutId(outId: String): Product

    @Query("""
        SELECT DISTINCT
            t1.out_group_name
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
        AND t1.out_group_id IS NOT NULL
    """, nativeQuery = true)
    fun findByOutName(): MutableList<ProductDto>

    @Query("""
        SELECT t1.*
        FROM crm_product t1
        where t1.enabled=1
        and t1.out_group_id in ?1
    """, nativeQuery = true)
    fun findByOutGroupIds(outGroupIds: MutableList<String>): MutableList<Product>

    fun findByProductTypeId(productTypeId: String): MutableList<Product>

//    fun updateProductTypeId(@Param("productTypeId") id: String, @Param("list") ids: MutableList<String>): Int

//    fun updateProductTypeToNull(productTypeId: String): Int

    fun findByOutGroupIdInAndAllowOrder(outGroupIds: MutableList<String>, allowOrder: Boolean): MutableList<ProductDto>

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
    fun findProductTypeList(): MutableList<ProductType>

    @Query("""
        SELECT
	t1.*
FROM
	crm_product t1
WHERE
	t1.product_id IN (
		SELECT
			product_id
		FROM
			crm_pricesystem_detail t2
		WHERE
			t2.pricesystem_id = :pricesystemId1)
		AND t1.product_id IN (
			SELECT
				product_id
			FROM
				crm_pricesystem_detail t3
			WHERE
				t3.pricesystem_id = :pricesystemId2)
			AND t1.enabled = 1
    """, nativeQuery = true)
    fun findIntersectionOfBothPricesystem(@Param("pricesystemId1") pricesystemId1: String, @Param("pricesystemId2") pricesystemId2: String): MutableList<ProductDto>

    fun findByNameIn(nameList: MutableList<String>): MutableList<Product>
}

interface ProductRepositoryCustom{

    fun findFilter(productQuery: ProductQuery): MutableList<Product>

    fun findPage(pageable: Pageable, productQuery: ProductQuery): Page<ProductDto>
}

class ProductRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ProductRepositoryCustom{

    override fun findFilter(productQuery: ProductQuery): MutableList<Product>{
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), Product::class.java)

        return query.resultList as MutableList<Product>
    }

    override fun findPage(pageable: Pageable, productQuery: ProductQuery): Page<ProductDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), ProductDto::class.java)

        return query.resultList as Page<ProductDto>
    }
}
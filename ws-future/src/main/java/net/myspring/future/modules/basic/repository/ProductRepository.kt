package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Product
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.ProductDto
import net.myspring.future.modules.basic.web.query.ProductQuery
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery
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
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("products"))
interface ProductRepository : BaseRepository<Product,String>,ProductRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Product

    override fun findAll(): MutableList<Product>

    @CachePut(key = "#p0.id")
    fun save(product: Product): Product

    fun findByEnabledIsTrueAndIdIn(idList: MutableList<String>): MutableList<Product>


    @Query("""
        SELECT t
        FROM #{#entityName} t
        where t.enabled=1
    """)
    fun findAllEnabled(): MutableList<Product>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
        AND t1.hasIme = 1
    """)
    fun findHasImeProduct(): MutableList<Product>

    @Query("""
        SELECT
            t
        FROM
            #{#entityName} t
        WHERE
            t.enabled = 1
        AND
            t.name LIKE concat( '%', ?1,'%')
    """)
    fun findByNameLike(name: String): MutableList<Product>

    @Query("""
        SELECT
            t
        FROM
            #{#entityName} t
        WHERE
            t.enabled = 1
        AND
            t.code LIKE concat( '%', ?1,'%')
        AND t.hasIme=1
    """)
    fun findByCodeLike(code: String): MutableList<Product>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
        AND t1.hasIme = 1
        AND t1.name LIKE concat( '%', ?1,'%')
    """)
    fun findByNameLikeHasIme(name: String): MutableList<Product>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
        AND t1.hasIme = 1
        AND t1.code LIKE concat( '%', ?1,'%')
    """)
    fun findByCodeLikeHasIme(code: String): MutableList<Product>

    fun findByName(name: String): Product

    fun findByEnabledIsTrueAndName(name: String): Product?

    fun findByEnabledIsTrueAndNameIn(nameList: MutableList<String>): MutableList<Product>

    fun findByOutId(outId: String): Product


    fun findByOutGroupIdIn(outGroupIds: MutableList<String>): MutableList<Product>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
        AND t1.productTypeId in ?1
    """)
    fun findByProductTypeIds(productTypeIds: MutableList<String>): MutableList<Product>

    fun findByEnabledIsTrueAndProductTypeId(productTypeId: String): MutableList<Product>

//    fun updateProductTypeId(@Param("productTypeId") id: String, @Param("list") ids: MutableList<String>): Int

//    fun updateProductTypeToNull(productTypeId: String): Int

    fun findByOutGroupIdInAndAllowOrderIsTrue(outGroupIds: MutableList<String>): MutableList<Product>

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

    fun findByEnabledIsTrue(): List<Product>
}

interface ProductRepositoryCustom{

    fun findByOutName(): MutableList<String>

    fun findByNameOrCodeAndOutGroupName(nameOrCode:String,outGroupName:String):MutableList<ProductDto>

    fun findFilter(productQuery: ProductQuery): MutableList<ProductDto>

    fun findFilterForAdpricesystemChange(adPricesystemChangeQuery: AdPricesystemChangeQuery):MutableList<ProductDto>

    fun findPage(pageable: Pageable, productQuery: ProductQuery): Page<ProductDto>
}

class ProductRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ProductRepositoryCustom{

    override fun findByOutName(): MutableList<String>{
        return namedParameterJdbcTemplate.queryForList("""
        SELECT DISTINCT
            t.out_group_name
        FROM
            crm_product t
        WHERE
            t.enabled = 1
        AND t.out_group_id IS NOT NULL
        """, HashMap<String, Any>(),String::class.java)
    }

    override fun findByNameOrCodeAndOutGroupName(nameOrCode:String,outGroupName:String):MutableList<ProductDto>{
        val params = HashMap<String, Any>()
        params.put("nameOrCode", nameOrCode)
        params.put("outGroupName", outGroupName)

        return namedParameterJdbcTemplate.query("""
            SELECT
                t.*
            FROM
                crm_product t
            WHERE
                t.enabled = 1
            AND t.name LIKE CONCAT('%',:nameOrCode,'%')
            OR t.code LIKE CONCAT('%',:nameOrCode,'%')
            AND t.out_group_name = :outGroupName
          """, params, BeanPropertyRowMapper(ProductDto::class.java))
    }

    override fun findFilterForAdpricesystemChange(adPricesystemChangeQuery: AdPricesystemChangeQuery):MutableList<ProductDto>{
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_product t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(adPricesystemChangeQuery.productName)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:productName,'%') """)
        }
        if (CollectionUtil.isNotEmpty(adPricesystemChangeQuery.productCodeLists)) {
            sb.append("""  and t1.code in (:productCodeLists) """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(adPricesystemChangeQuery), BeanPropertyRowMapper(ProductDto::class.java))
    }

    override fun findFilter(productQuery: ProductQuery): MutableList<ProductDto>{
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_product t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(productQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(productQuery.code)) {
            sb.append("""  and t1.code LIKE CONCAT('%',:code,'%') """)
        }
        if (productQuery.hasIme != null && productQuery.hasIme) {
            sb.append("""  and t1.has_ime = 1 """)
        }
        if (productQuery.hasIme != null && !productQuery.hasIme) {
            sb.append("""  and t1.has_ime = 0 """)
        }
        if (productQuery.allowOrder !=null && productQuery.allowOrder) {
            sb.append("""  and t1.allow_order =1 """)
        }
        if (productQuery.allowOrder !=null && !productQuery.allowOrder) {
            sb.append("""  and t1.allow_order =0 """)
        }
        if (productQuery.visible !=null && productQuery.visible ) {
            sb.append("""  and t1.visible = 1 """)
        }
        if (productQuery.visible !=null && !productQuery.visible ) {
            sb.append("""  and t1.visible = 0 """)
        }
        if (StringUtils.isNotEmpty(productQuery.productTypeId)) {
            sb.append("""  and t1.product_type_id =:productTypeId """)
        }
        if (StringUtils.isNotEmpty(productQuery.outGroupName)) {
            sb.append("""  and t1.out_group_name =:outGroupName """)
        }
        if (StringUtils.isNotEmpty(productQuery.netType)) {
            sb.append("""  and t1.net_type =:netType """)
        }
        if (StringUtils.isNotEmpty(productQuery.netType)) {
            sb.append("""  and t1.net_type =:netType """)
        }
        if (CollectionUtil.isNotEmpty(productQuery.ids)) {
            sb.append("""  and t1.id in (:ids) """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productQuery), BeanPropertyRowMapper(ProductDto::class.java))
    }

    override fun findPage(pageable: Pageable, productQuery: ProductQuery): Page<ProductDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_product t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(productQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(productQuery.code)) {
            sb.append("""  and t1.code LIKE CONCAT('%',:code,'%') """)
        }
        if (productQuery.hasIme != null && productQuery.hasIme) {
            sb.append("""  and t1.has_ime = 1 """)
        }
        if (productQuery.hasIme != null && !productQuery.hasIme) {
            sb.append("""  and t1.has_ime = 0 """)
        }
        if (productQuery.allowOrder !=null && productQuery.allowOrder) {
            sb.append("""  and t1.allow_order =1 """)
        }
        if (productQuery.allowOrder !=null && !productQuery.allowOrder) {
            sb.append("""  and t1.allow_order =0 """)
        }
        if (productQuery.visible !=null && productQuery.visible ) {
            sb.append("""  and t1.visible = 1 """)
        }
        if (productQuery.visible !=null && !productQuery.visible ) {
            sb.append("""  and t1.visible = 0 """)
        }
        if (StringUtils.isNotEmpty(productQuery.productTypeId)) {
            sb.append("""  and t1.product_type_id =:productTypeId """)
        }
        if (StringUtils.isNotEmpty(productQuery.outGroupName)) {
            sb.append("""  and t1.out_group_name =:outGroupName """)
        }
        if (StringUtils.isNotEmpty(productQuery.netType)) {
            sb.append("""  and t1.net_type =:netType """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(productQuery), BeanPropertyRowMapper(ProductDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(productQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
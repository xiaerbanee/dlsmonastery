package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ProductType
import net.myspring.future.modules.basic.dto.ProductTypeDto
import net.myspring.future.modules.basic.web.query.ProductTypeQuery
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


@CacheConfig(cacheNames = arrayOf("productTypes"))
interface ProductTypeRepository : BaseRepository<ProductType,String>,ProductTypeRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): ProductType

    override fun findAll(): MutableList<ProductType>

    @CachePut(key = "#p0.id")
    fun save(productType: ProductType): ProductType

    @Query("""
    SELECT t
    FROM #{#entityName} t
    WHERE t.enabled = 1
    AND t.name LIKE CONCAT('%',?1,'%')
    """)
    fun findByNameLike(name: String): MutableList<ProductType>

    fun findByNameAndCompanyId(name :String,  companyId :String): ProductType?

    @Query("""
        SELECT t1
        from #{#entityName}  t1
        where t1.scoreType=1
        and t1.enabled = 1
        and t1.price1 is not null order by t1.name
    """)
    fun findAllScoreType(): MutableList<ProductType>

    fun findByDemoPhoneTypeId(demoPhoneTypeId: String): MutableList<ProductType>
}

interface ProductTypeRepositoryCustom{
    fun findPage(pageable: Pageable, productTypeQuery: ProductTypeQuery): Page<ProductTypeDto>

     fun findDto(id: String): ProductTypeDto
}

class ProductTypeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ProductTypeRepositoryCustom{
    override fun findDto(id: String): ProductTypeDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
                group_concat(t2.name) AS productNames, group_concat(t2.id) AS productIds, t1.*
            FROM
                crm_product_type t1
            LEFT JOIN crm_product t2 ON t1.id = t2.product_type_id
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.id = :id
            GROUP BY
                t1.id
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ProductTypeDto::class.java))

    }

    override fun findPage(pageable: Pageable, productTypeQuery: ProductTypeQuery): Page<ProductTypeDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                group_concat(t2.name) AS productNames, group_concat(t2.id) AS productIds, t1.*
            FROM
                crm_product_type t1
            LEFT JOIN crm_product t2 ON t1.id = t2.product_type_id
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
        """)
        if (StringUtils.isNotEmpty(productTypeQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        if (StringUtils.isNotEmpty(productTypeQuery.code)) {
            sb.append("""  and t1.code LIKE CONCAT('%',:code,'%') """)
        }
        sb.append(""" group by t1.id """)


        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val paramMap = BeanPropertySqlParameterSource(productTypeQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductTypeDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
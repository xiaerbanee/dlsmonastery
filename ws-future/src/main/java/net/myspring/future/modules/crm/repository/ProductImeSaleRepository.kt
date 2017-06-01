package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import net.myspring.common.dto.NameValueDto
import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.modules.crm.domain.ProductImeSale
import net.myspring.future.modules.crm.dto.*
import net.myspring.future.modules.crm.web.query.*
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ProductImeSaleRepository : BaseRepository<ProductImeSale, String>, ProductImeSaleRepositoryCustom {

    fun findByProductImeId(productImeId: String): MutableList<ProductImeSale>

    @Query("""
    SELECT
        t1
    FROM
        #{#entityName} t1
    WHERE
        t1.employeeId = :employeeId
    AND t1.createdDate >= :createdDateStart
    AND t1.createdDate <= :createdDateEnd
        """ )
    fun findByEmployeeId(@Param("employeeId") employeeId: String, @Param("createdDateStart") createdDateStart: LocalDateTime, @Param("createdDateEnd") createdDateEnd: LocalDateTime): MutableList<ProductImeSale>

    fun findTopByEnabledIsTrueAndEmployeeIdOrderByCreatedDateDesc(employeeId: String): ProductImeSale



}



interface ProductImeSaleRepositoryCustom{
    fun findPage(pageable: Pageable, productImeSaleQuery: ProductImeSaleQuery): Page<ProductImeSaleDto>

    fun findDto(id: String?): ProductImeSaleDto?
}

class ProductImeSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeSaleRepositoryCustom {
    override fun findDto(id: String?): ProductImeSaleDto? {
        if(id == null){
            return null
        }
        val  result = namedParameterJdbcTemplate.query("""
        SELECT
            ime.ime productImeIme,
            ime.meid productImeMeid,
            ime.product_id productImeProductId,
            depot.office_id shopOfficeId,
            t1.*
        FROM
            crm_product_ime_sale t1,
            crm_depot depot,
            crm_product_ime ime
        WHERE
            t1.id = :id
        AND t1.product_ime_id = ime.id
        AND t1.shop_id = depot.id
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ProductImeSaleDto::class.java));
        if(result!=null && result.size > 0){
            return result[0]
        }else {
            return null
        }
    }

    override fun findPage(pageable: Pageable, productImeSaleQuery: ProductImeSaleQuery): Page<ProductImeSaleDto> {

        val sb = StringBuffer()
        sb.append("""
        SELECT  *
        FROM
        (
            select
              ime.ime productImeIme, ime.meid productImeMeid, ime.product_id productImeProductId, depot.office_id shopOfficeId,  t1.*
            from
              crm_product_ime_sale t1,crm_depot depot, crm_product_ime ime
            where
            t1.enabled=1
            and t1.product_ime_id = ime.id
            and t1.shop_id=depot.id
        """)

        if (productImeSaleQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if (productImeSaleQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date <  :createdDateEnd """)
        }
        if (StringUtils.isNotBlank(productImeSaleQuery.shopName )) {
            sb.append("""  and depot.name LIKE CONCAT('%', :shopName,'%')  """)
        }
        if (productImeSaleQuery.isBack != null && productImeSaleQuery.isBack) {
            sb.append("""  and t1.is_back = 1  """)
        }
        if (productImeSaleQuery.isBack != null && !productImeSaleQuery.isBack) {
            sb.append("""  and t1.is_back = 0  """)
        }
        if (StringUtils.isNotBlank(productImeSaleQuery.ime )) {
            sb.append("""  and t1.ime LIKE CONCAT('%', :ime,'%')  """)
        }
        if (StringUtils.isNotBlank(productImeSaleQuery.employeeId )) {
            sb.append("""  and t1.employee_id = :employeeId  """)
        }
        sb.append("""
        ) result
        """)

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var paramMap = BeanPropertySqlParameterSource(productImeSaleQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductImeSaleDto::class.java))

        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}
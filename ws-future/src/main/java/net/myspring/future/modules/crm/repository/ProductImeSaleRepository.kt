package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductImeSale
import net.myspring.future.modules.crm.dto.ProductImeSaleDto
import net.myspring.future.modules.crm.web.query.ProductImeSaleQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
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

    fun findDto(id: String): ProductImeSaleDto
}

class ProductImeSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeSaleRepositoryCustom {
    override fun findDto(id: String): ProductImeSaleDto {
        return namedParameterJdbcTemplate.queryForObject("""
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
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ProductImeSaleDto::class.java))

    }

    override fun findPage(pageable: Pageable, productImeSaleQuery: ProductImeSaleQuery): Page<ProductImeSaleDto> {

        val sb = StringBuilder()
        sb.append("""
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
            t1.enabled = 1
            AND t1.shop_id = depot.id
            AND t1.product_ime_id = ime.id
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

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val paramMap = BeanPropertySqlParameterSource(productImeSaleQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductImeSaleDto::class.java))

        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}
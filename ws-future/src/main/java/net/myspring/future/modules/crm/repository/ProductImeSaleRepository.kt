package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductImeSale
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.dto.ProductImeForSaleDto
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

    fun findTopByEnabledIsTrueAndEmployeeIdOrderByCreatedDateDesc(employeeId: String): ProductImeSale?



}



interface ProductImeSaleRepositoryCustom{
    fun findPage(pageable: Pageable, productImeSaleQuery: ProductImeSaleQuery): Page<ProductImeSaleDto>

    fun findDto(id: String): ProductImeSaleDto

    fun findForBatchUpload(dateStart: LocalDateTime, dateEnd: LocalDateTime, officeIds: List<String>): List<ProductImeSaleDto>

    fun findProductImeForSaleDto(imeList: List<String>, companyId: String): List<ProductImeForSaleDto>
}

class ProductImeSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeSaleRepositoryCustom {
    override fun findProductImeForSaleDto(imeList: List<String>, companyId: String): List<ProductImeForSaleDto> {
        if(imeList.isEmpty()){
            return ArrayList()
        }

        val params = HashMap<String, Any>()
        params.put("imeList", imeList)
        params.put("companyId", companyId)
        return namedParameterJdbcTemplate.query("""
        SELECT
            sale.created_date productImeSaleCreatedDate,
            sale.employee_id productImeSaleEmployeeId,
            sale.shop_id productImeSaleShopId,
            sale.id productImeSaleId,
            upload.created_date productImeUploadCreatedDate,
            validProductIme.*
        FROM
        (
            SELECT
            product.product_type_id, depot.office_id depotOfficeId, depot.chain_id depotChainId, depot.depot_store_id depotDepotStoreId, t1.*
            FROM
            crm_product_ime t1,
            crm_depot depot,
            crm_product product
            WHERE
                t1.enabled = 1
                AND t1.depot_id = depot.id
                AND depot.enabled = 1
                AND t1.company_id =  :companyId
                AND t1.product_id =  product.id
                AND product.enabled =  1
                AND t1.ime in (:imeList)
            ) validProductIme
            LEFT JOIN crm_product_ime_sale sale ON validProductIme.product_ime_sale_id = sale.id AND sale.enabled = 1
            LEFT JOIN crm_product_ime_upload upload ON validProductIme.product_ime_upload_id = upload.id AND upload.enabled = 1
                """, params, BeanPropertyRowMapper(ProductImeForSaleDto::class.java))
    }

    override fun findForBatchUpload(dateStart: LocalDateTime, dateEnd: LocalDateTime, officeIds: List<String>): List<ProductImeSaleDto> {
      if(officeIds.isEmpty()){
          return ArrayList()
      }

        val params = HashMap<String, Any>()
        params.put("officeIds", officeIds)
        params.put("dateStart", dateStart)
        params.put("dateEnd", dateEnd)

        return namedParameterJdbcTemplate.query("""
        SELECT
            ime.ime productImeIme,
            ime.meid productImeMeid,
            ime.product_id productImeProductId,
            ime.product_ime_upload_id productImeProductImeUploadId,
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            depotShop.area_type depotShopAreaType,
            t1.*
        FROM
            crm_product_ime_sale t1
            LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            LEFT JOIN crm_depot_shop depotShop ON depot.depot_shop_id = depotShop.id
            LEFT JOIN crm_product_ime ime ON t1.product_ime_id = ime.id

        WHERE
            t1.enabled = 1
            AND t1.is_back = 0
            AND depot.office_id IN (:officeIds)
            AND t1.created_date >= (:dateStart)
            AND t1.created_date < (:dateEnd)
                """, params, BeanPropertyRowMapper(ProductImeSaleDto::class.java))

    }

    override fun findDto(id: String): ProductImeSaleDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            ime.ime productImeIme,
            ime.meid productImeMeid,
            ime.product_id productImeProductId,
            ime.product_ime_upload_id productImeProductImeUploadId,
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            depotShop.area_type depotShopAreaType,
            t1.*
        FROM
            crm_product_ime_sale t1
            LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            LEFT JOIN crm_depot_shop depotShop ON depot.depot_shop_id = depotShop.id
            LEFT JOIN crm_product_ime ime ON t1.product_ime_id = ime.id
        WHERE
            t1.id = :id
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ProductImeSaleDto::class.java))
    }

    override fun findPage(pageable: Pageable, productImeSaleQuery: ProductImeSaleQuery): Page<ProductImeSaleDto> {

        val sb = StringBuilder()
        sb.append("""
        SELECT
            ime.ime productImeIme,
            ime.meid productImeMeid,
            ime.product_id productImeProductId,
            ime.product_ime_upload_id productImeProductImeUploadId,
            ime.retail_date retailDate,
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            depotShop.area_type depotShopAreaType,
            t1.*
        FROM
            crm_product_ime_sale t1
            LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            LEFT JOIN crm_depot_shop depotShop ON depot.depot_shop_id = depotShop.id
            LEFT JOIN crm_product_ime ime ON t1.product_ime_id = ime.id
        WHERE
            t1.enabled = 1
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
        if (StringUtils.isNotBlank(productImeSaleQuery.imeReverse )) {
            sb.append("""  and ime.ime_reverse LIKE CONCAT( :imeReverse,'%')  """)
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
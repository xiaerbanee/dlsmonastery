package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductImeUpload
import net.myspring.future.modules.crm.dto.ProductImeUploadDto
import net.myspring.future.modules.crm.dto.ReportImeUploadDto
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery
import net.myspring.future.modules.crm.web.query.ProductMonthPriceSumQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import kotlin.collections.HashMap


interface ProductImeUploadRepository : BaseRepository<ProductImeUpload, String>,  ProductImeUploadRepositoryCustom{
}

interface ProductImeUploadRepositoryCustom{

    fun findPage(pageable: Pageable, productImeUploadQuery : ProductImeUploadQuery): Page<ProductImeUploadDto>

    fun findDto(id: String): ProductImeUploadDto

    fun setDepotIdForMerge(fromDepotId:String,toDepotId:String):Int

    fun findProductTypesByMonth(month: String): List<Map<String,Any>>

    fun getReportData(productMonthPriceSumQuery: ProductMonthPriceSumQuery): MutableList<ReportImeUploadDto>

    fun findForBatchAudit(month: String, auditOfficeIds:List<String>): MutableList<ProductImeUpload>

    fun findImeUploads(productMonthPriceSumQuery: ProductMonthPriceSumQuery): MutableList<ProductImeUploadDto>

}

class ProductImeUploadRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeUploadRepositoryCustom {
    override fun findProductTypesByMonth(month: String): List<Map<String, Any>> {
        val sb = StringBuilder("""
                     SELECT
                            distinct t1.id  productTypeId,
                            t1.name  productTypeName
                        FROM
                            crm_product_ime_upload t,
                            crm_product_type t1
                        WHERE
                            t.enabled =1
                            AND t1.enabled =1
                            AND t1.id = t.product_type_id
                            AND t.month=:month
                    """)
        return namedParameterJdbcTemplate.queryForList(sb.toString(),Collections.singletonMap("month",month))
    }

    override fun findImeUploads(productMonthPriceSumQuery: ProductMonthPriceSumQuery): MutableList<ProductImeUploadDto> {
        val sb = StringBuilder("""
                    SELECT
                        ime.ime productImeIme,
                        ime.product_id productImeProductId,
                        depot.office_id shopOfficeId,
                        depot.area_id shopAreaId,
                        t1.*
                    FROM
                        crm_product_ime_upload t1,
                        crm_depot depot,
                        crm_product_ime ime
                    WHERE
                        t1.enabled = 1
                        AND t1.shop_id = depot.id
                        AND t1.product_ime_id = ime.id
                    """)
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.status)) {
            sb.append(""" and t1.status = :status """)
        }
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.month)) {
            sb.append("""  and t1.month = :month  """)
        }
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.areaId)) {
            sb.append(""" and depot.area_id = :areaId  """)
        }

        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productMonthPriceSumQuery), BeanPropertyRowMapper(ProductImeUploadDto::class.java))

    }

    override fun findForBatchAudit(month: String, auditOfficeIds: List<String>): MutableList<ProductImeUpload> {
        val sb = StringBuilder("""
                    SELECT
                        t1.*
                    FROM
                        crm_product_ime_upload t1,
                        crm_depot t2
                    WHERE
                        t1.enabled =1
                    AND t2.enabled =1
                    AND t1.shop_id = t2.id
                    AND t1.month = :month
                    AND t2.office_id in (:auditOfficeIds) """)
        val params = HashMap<String, Any>()
        params.put("month", month)
        params.put("auditOfficeIds", auditOfficeIds)
        return namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper(ProductImeUpload::class.java))
    }

    override fun getReportData(productMonthPriceSumQuery: ProductMonthPriceSumQuery): MutableList<ReportImeUploadDto> {
        val sb = StringBuilder("""
                SELECT
                    t1.shop_id shopId,
                    t1.product_type_id productTypeId,
                    t1.employee_id employeeId,
                    t2.office_id officeId,
	                t2.area_id areaId,
                    count(*) qty
                FROM
                    crm_product_ime_upload t1,
                    crm_depot t2
               where
	                t1.enabled = 1
	                and t1.shop_id = t2.id
	                and t2.enabled = 1
                AND
                    t1.month = :month
            """)
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.status)) {
            sb.append(""" AND t1.status = :status """)
        }
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.areaId)) {
            sb.append(""" AND t2.area_id = :areaId """)
        }
        if (CollectionUtil.isNotEmpty(productMonthPriceSumQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        sb.append(""" GROUP BY t1.shop_id,t1.product_type_id,t1.employee_id """)

        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productMonthPriceSumQuery), BeanPropertyRowMapper(ReportImeUploadDto::class.java))
    }

    override fun findDto(id: String): ProductImeUploadDto {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT
            ime.ime productImeIme,
            t1.*
        FROM
            crm_product_ime_upload t1,
            crm_product_ime ime
        WHERE
            t1.enabled = 1
            AND t1.product_ime_id = ime.id
            AND ime.enabled = 1
            AND t1.id = :id
                """, Collections.singletonMap("id", id), BeanPropertyRowMapper(ProductImeUploadDto::class.java))
    }

    override fun findPage(pageable: Pageable, productImeUploadQuery: ProductImeUploadQuery): Page<ProductImeUploadDto> {

        val sb = StringBuilder("""
        SELECT
            ime.ime productImeIme,
            ime.product_id productImeProductId,
            depot.office_id shopOfficeId,
            depot.area_id shopAreaId,
            t1.*
        FROM
            crm_product_ime_upload t1,
            crm_depot depot,
            crm_product_ime ime
        WHERE
            t1.enabled = 1
            AND t1.shop_id = depot.id
            AND t1.product_ime_id = ime.id
        """)
        if (productImeUploadQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date >= :createdDateStart  """)
        }
        if (productImeUploadQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date < :createdDateEnd  """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId  """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.month)) {
            sb.append("""  and t1.month = :month  """)
        }
        if (CollectionUtil.isNotEmpty(productImeUploadQuery.imeOrMeidList)) {
            sb.append("""  and (ime.ime in (:imeOrMeidList) or ime.ime2 in (:imeOrMeidList) or ime.meid in (:imeOrMeidList))  """)
        }
        if (CollectionUtil.isNotEmpty(productImeUploadQuery.depotIdList)) {
            sb.append("""  and depot.id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(productImeUploadQuery.officeIdList)) {
            sb.append("""  and depot.office_id in (:officeIdList) """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val paramMap = BeanPropertySqlParameterSource(productImeUploadQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductImeUploadDto::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }

    override fun setDepotIdForMerge(fromDepotId:String,toDepotId:String):Int{
        val params = HashMap<String, Any>()
        params.put("fromDepotId", fromDepotId)
        params.put("toDepotId", toDepotId)

        val sb = java.lang.StringBuilder()
        sb.append(""" update crm_product_ime_upload t set t.shop_id = :toDepotId where t.shop_id = :fromDepotId """)
        return namedParameterJdbcTemplate.update(sb.toString(),params)
    }
}
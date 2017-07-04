package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductImeUpload
import net.myspring.future.modules.crm.dto.ProductImeUploadDto
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery
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


interface ProductImeUploadRepository : BaseRepository<ProductImeUpload, String>,  ProductImeUploadRepositoryCustom{

    fun findByProductImeId(productImeId: String): MutableList<ProductImeUpload>

}

interface ProductImeUploadRepositoryCustom{
    fun findPage(pageable: Pageable, productImeUploadQuery : ProductImeUploadQuery): Page<ProductImeUploadDto>

    fun findDto(id: String): ProductImeUploadDto
}

class ProductImeUploadRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ProductImeUploadRepositoryCustom {

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

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val paramMap = BeanPropertySqlParameterSource(productImeUploadQuery)
        val list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(ProductImeUploadDto::class.java))
        return PageImpl(list,pageable,((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}
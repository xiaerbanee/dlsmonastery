package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.dto.ProductImeUploadDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl

import javax.persistence.EntityManager


interface ProductImeUploadRepository : BaseRepository<ProductImeUpload, String>,  ProductImeUploadRepositoryCustom{

    fun findByProductImeId(productImeId: String): MutableList<ProductImeUpload>

}

interface ProductImeUploadRepositoryCustom{
    fun findPage(pageable: Pageable, productImeUploadQuery : ProductImeUploadQuery): Page<ProductImeUploadDto>
}

class ProductImeUploadRepositoryImpl @Autowired constructor(val entityManager: EntityManager): ProductImeUploadRepositoryCustom {
    override fun findPage(pageable: Pageable, productImeUploadQuery: ProductImeUploadQuery): Page<ProductImeUploadDto> {

        val sb = StringBuffer()
        sb.append("""
        select
         ime.ime productImeIme, t1.*
        from
         crm_product_ime_upload t1,crm_depot depot, crm_product_ime ime
        where
            t1.enabled=1
            and t1.shop_id=depot.id
            and depot.enabled = 1
            and t1.product_ime_id = ime.id
            and ime.enabled = 1
        """)
        if (productImeUploadQuery.createdDateStart != null) {
            sb.append("""
                and t1.created_date >= :createdDateStart
            """)
        }
        if (productImeUploadQuery.createdDateEnd != null) {
            sb.append("""
                and t1.created_date < :createdDateEnd
            """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.shopName)) {
            sb.append("""
                and depot.name LIKE CONCAT('%', :shopName, '%')
            """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.officeId)) {
            sb.append("""
               and depot.office_id = :officeId
            """)
        }
        if (StringUtils.isNotBlank(productImeUploadQuery.month)) {
            sb.append("""
                and t1.month = :month
            """)
        }
        if (productImeUploadQuery.imeOrMeidList != null) {
            sb.append("""
                and (ime.ime in :imeOrMeidList or ime.ime2 in :imeOrMeidList or ime.meid in :imeOrMeidList)
            """)
        }

        val queryStr = QueryUtils.getMySQLDialect().getPageableSql(sb.toString(), pageable)

        val query = entityManager.createNativeQuery(queryStr, ProductImeUploadDto::class.java)
        QueryUtils.setParameter(query, pageable, productImeUploadQuery)

        val result = query.resultList

        return PageImpl<ProductImeUploadDto>(result as MutableList<ProductImeUploadDto>, pageable, ((pageable.pageNumber + 100) * pageable.pageSize).toLong())

    }


}
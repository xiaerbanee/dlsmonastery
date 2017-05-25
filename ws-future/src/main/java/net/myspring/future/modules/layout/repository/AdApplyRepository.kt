package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdApply
import net.myspring.future.modules.layout.dto.AdApplyDto
import net.myspring.future.modules.layout.web.query.AdApplyQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdApplyRepository : BaseRepository<AdApply,String>,AdApplyRepositoryCustom {

    @Query("""
        select t.id
        from crm_ad_apply t
        where t.enabled = 1
    """, nativeQuery = true)
    //TODO 修改该query
    fun findByOutGroupIdAndDate(@Param("dateStart") dateStart: LocalDate, @Param("outGroupIds") outGroupIds: List<String>): List<AdApplyDto>

    @Query("""
        select t.id
        from crm_ad_apply t
        where t.enabled = 1
    """, nativeQuery = true)
    fun findAllId(): List<String>

    @Query("""
        select t.id
        from crm_ad_apply t
        where t.enabled = 1
    """, nativeQuery = true)
            //TODO 修改该query
    fun findByFilter(@Param("p") map: Map<String, Any>): List<AdApply>

}

interface AdApplyRepositoryCustom{
    fun findPage(pageable: Pageable,adApplyQuery: AdApplyQuery): Page<AdApplyDto>
}

class AdApplyRepositoryImpl @Autowired constructor(val entityManager: EntityManager):AdApplyRepositoryCustom{

    override fun findPage(pageable: Pageable,adApplyQuery: AdApplyQuery): Page<AdApplyDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_ad_apply t1,
                crm_product product
            WHERE
                t1.enabled = 1
            AND t1.product_id = product.id
        """)
        if (StringUtils.isNotEmpty(adApplyQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.createdBy)) {
            sb.append("""  and t1.create_by = :createdBy """)
        }
        if (adApplyQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adApplyQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productName)) {
            sb.append(""" and product.name like CONCAT('%', :productName,'%') """)
        }
        /*if (adApplyQuery.billed != null) {
            sb.append("""
                and t1.billed_qty
            """)
        }*/
        var query = entityManager.createNativeQuery(sb.toString(),AdApplyDto::class.java)

        return query.resultList as Page<AdApplyDto>
    }
}
package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdGoodsOrder
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdGoodsOrderRepository : BaseRepository<AdGoodsOrder,String>,AdGoodsOrderRepositoryCustom{
    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM
        #{#entityName} t1
    WHERE
        t1.createdDate >= ?1
    """)
    fun findMaxBusinessId(localDate: LocalDate): String
}

interface AdGoodsOrderRepositoryCustom{
    fun findPage(pageable: Pageable,adGoodsOrderQuery: AdGoodsOrderQuery): Page<AdGoodsOrderDto>
}

class AdGoodsOrderRepositoryImpl @Autowired constructor(val entityManager: EntityManager):AdGoodsOrderRepositoryCustom{

    override fun findPage(pageable: Pageable,adGoodsOrderQuery: AdGoodsOrderQuery): Page<AdGoodsOrderDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                depot.office_id officeId,
                t1.*
            FROM
                crm_ad_goods_order t1,
                crm_depot depot
            WHERE
                t1.enabled = 1
            AND t1.shop_id = depot.id
        """)
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.billType)) {
            sb.append("""  and t1.bill_type = :billType """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.remarks)) {
            sb.append("""  and t1.remarks = :remarks """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.storeId)) {
            sb.append("""  and t1.store_id = :storeId """)
        }
        if (StringUtils.isNotEmpty(adGoodsOrderQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (adGoodsOrderQuery.billDateStart != null) {
            sb.append("""  and t1.bill_date  >= :billDateStart """)
        }
        if (adGoodsOrderQuery.billDateEnd != null) {
            sb.append("""  and t1.bill_date  < :billDateEnd """)
        }
        if (adGoodsOrderQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adGoodsOrderQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }

        var query = entityManager.createNativeQuery(sb.toString(),AdGoodsOrderDto::class.java)

        return query.resultList as Page<AdGoodsOrderDto>
    }
}
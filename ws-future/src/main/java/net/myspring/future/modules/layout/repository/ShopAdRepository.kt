package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAd
import net.myspring.future.modules.layout.dto.ShopAdDto
import net.myspring.future.modules.layout.web.query.ShopAdQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAdRepository : BaseRepository<ShopAd,String>,ShopAdRepositoryCustom {

}

interface ShopAdRepositoryCustom{
    fun findPage(pageable: Pageable,shopAdQuery: ShopAdQuery): Page<ShopAdDto>

    fun findByFilter(shopAdQuery: ShopAdQuery): MutableList<ShopAdDto>
}

class ShopAdRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopAdRepositoryCustom{

    override fun findPage(pageable: Pageable,shopAdQuery: ShopAdQuery): Page<ShopAdDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                depot.office_id officeId,t1.*
            FROM
                crm_shop_ad t1,crm_depot depot
            WHERE
                t1.enabled=1
            and t1.shop_id=depot.id
        """)
        if (StringUtils.isNotEmpty(shopAdQuery.shopAdTypeId)) {
            sb.append("""  and t1.shop_ad_type_id = :shopAdTypeId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.specialArea)) {
            sb.append("""  and t1.special_area = :specialArea """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.ids)) {
            sb.append("""  and t1.id in :ids """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId """)
        }
        if (shopAdQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (shopAdQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }

        var query = entityManager.createNativeQuery(sb.toString(),ShopAdDto::class.java)

        return query.resultList as Page<ShopAdDto>

    }

    override fun findByFilter(shopAdQuery: ShopAdQuery): MutableList<ShopAdDto>{
        val sb = StringBuffer()
        sb.append("""
            SELECT
                depot.office_id officeId,t1.*
            FROM
                crm_shop_ad t1,crm_depot depot
            WHERE
                t1.enabled=1
            and t1.shop_id=depot.id
        """)
        if (StringUtils.isNotEmpty(shopAdQuery.shopAdTypeId)) {
            sb.append("""  and t1.shop_ad_type_id = :shopAdTypeId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.specialArea)) {
            sb.append("""  and t1.special_area = :specialArea """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.ids)) {
            sb.append("""  and t1.id in :ids """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId """)
        }
        if (shopAdQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (shopAdQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateStart """)
        }

        var query = entityManager.createNativeQuery(sb.toString(),ShopAdDto::class.java)

        return query.resultList as MutableList<ShopAdDto>
    }
}
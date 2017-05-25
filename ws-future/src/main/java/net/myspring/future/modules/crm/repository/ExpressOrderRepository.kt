package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String>, ExpressOrderRepositoryCustom {


    fun findByExtendIdAndExtendType(extendId: String, extendType: String): ExpressOrder

    @Query("""
    SELECT
        t1.*
    FROM
        crm_express_order t1
    WHERE
        t1.enabled=1 and t1.id = ?1
        """, nativeQuery = true)
    fun findDto(id: String): ExpressOrderDto

    @Query("""
    SELECT
        t1.*
    FROM
        crm_express_order t1, crm_goods_order t2
    WHERE
        t1.enabled=1  and t1.id = t2.express_order_id
        and t2.id = ?1
        """, nativeQuery = true)
    fun findDtoByGoodsOrderId(goodsOrderId: String): ExpressOrderDto

}

interface ExpressOrderRepositoryCustom{
    fun findPage(pageable : Pageable, expressOrderQuery : ExpressOrderQuery): Page<ExpressOrderDto>?
}

class ExpressOrderRepositoryImpl @Autowired constructor(val entityManager: EntityManager): ExpressOrderRepositoryCustom {
    override fun findPage(pageable : Pageable,expressOrderQuery: ExpressOrderQuery): Page<ExpressOrderDto>? {

        return null

    }


}
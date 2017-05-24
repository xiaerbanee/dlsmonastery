package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ExpressOrder

import net.myspring.future.modules.crm.domain.GoodsOrderDetail
import net.myspring.future.modules.crm.dto.ExpressOrderDto
import org.apache.ibatis.annotations.Param
import org.springframework.data.jpa.repository.Query


interface ExpressOrderRepository : BaseRepository<ExpressOrder, String> {


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

package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import net.myspring.future.modules.crm.domain.GoodsOrderDetail



interface GoodsOrderDetailRepository : BaseRepository<GoodsOrderDetail, String> {

    fun findByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderDetail>

}

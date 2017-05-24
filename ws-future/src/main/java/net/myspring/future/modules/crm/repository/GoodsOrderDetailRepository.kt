package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository

import net.myspring.future.modules.crm.domain.GoodsOrderDetail



interface GoodsOrderDetailRepository : BaseRepository<GoodsOrderDetail, String> {

    fun findByGoodsOrderId(goodsOrderId: String): List<GoodsOrderDetail>

    fun deleteById(id: String)



}

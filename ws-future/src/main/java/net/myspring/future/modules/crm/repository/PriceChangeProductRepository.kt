package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChangeProduct

import net.myspring.future.modules.crm.domain.PricesystemChange


interface PriceChangeProductRepository : BaseRepository<PriceChangeProduct, String> {

    fun findByPriceChangeId(priceChangeId: String): MutableList<PriceChangeProduct>
}


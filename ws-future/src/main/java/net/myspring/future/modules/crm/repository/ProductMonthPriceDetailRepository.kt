package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail


interface ProductMonthPriceDetailRepository : BaseRepository<ProductMonthPriceDetail, String> {

    fun deleteByProductMonthPriceId(productMonthPriceId: String): Int
}

package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystemDetail


interface AdPricesystemDetailRepository : BaseRepository<AdPricesystemDetail,String> {

    fun findByAdPricesystemId(adPricesystemId: String): MutableList<AdPricesystemDetail>

    fun findByEnabledIsTrueAndAdPricesystemId(adPricesystemId: String): MutableList<AdPricesystemDetail>

}
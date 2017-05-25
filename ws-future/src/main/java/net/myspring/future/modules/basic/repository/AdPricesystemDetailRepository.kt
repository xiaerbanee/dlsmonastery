package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystemDetail

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdPricesystemDetailRepository : BaseRepository<AdPricesystemDetail,String> {

    fun findByAdPricesystemId(adPricesystemId: String): MutableList<AdPricesystemDetail>
}
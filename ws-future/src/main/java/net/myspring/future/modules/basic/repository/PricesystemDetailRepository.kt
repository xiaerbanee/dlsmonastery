package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.PricesystemDetail
import org.apache.ibatis.annotations.Param

/**
 * Created by zhangyf on 2017/5/24.
 */
interface PricesystemDetailRepository : BaseRepository<PricesystemDetail,String>{

    fun findByPricesystemIds(pricesystemIds: List<String>): List<PricesystemDetail>

    fun findByPricesystemIdAndProductId(@Param("pricesystemId") pricesystemId: String, @Param("productId") productId: String): PricesystemDetail

    fun findByProductIds(productIdList: List<String>): List<PricesystemDetail>

    fun findByPricesystemId(pricesystemId: String): List<PricesystemDetail>

    fun findByDepotId(depotId: String): List<PricesystemDetail>
}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.PricesystemDetail
import org.apache.ibatis.annotations.Param
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
interface PricesystemDetailRepository : BaseRepository<PricesystemDetail,String>{

    fun findByPricesystemIdIn(pricesystemIds: List<String>): List<PricesystemDetail>

    fun findByPricesystemIdAndProductId( pricesystemId: String, productId: String): PricesystemDetail

    fun findByProductIdIn(productIdList: List<String>): List<PricesystemDetail>

    fun findByPricesystemId(pricesystemId: String): List<PricesystemDetail>

    @Query("""
        DELETE FROM
        crm_account_bank
        where bank_id=?1
    """, nativeQuery = true)
    //TODO 需要重写该sql
    fun findByDepotId(depotId: String): List<PricesystemDetail>
}
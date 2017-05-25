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
        SELECT
            t1.*
        FROM
            crm_pricesystem_detail t1,
            crm_depot t2
        WHERE
            t1.pricesystem_id = t2.pricesystem_id
        AND t2.id = ?1
    """, nativeQuery = true)
    fun findByDepotId(depotId: String): List<PricesystemDetail>
}
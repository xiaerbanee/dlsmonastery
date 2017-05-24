package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopGoodsDepositRepository : BaseRepository<ShopGoodsDeposit,String> {

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_goods_deposit t1
    WHERE
        t1.enabled = 1
    AND t1.shop_id = :shopId
    AND t1.status = :status
    ORDER BY
        created_date DESC
    """, nativeQuery = true)
    fun findByShopId(@Param("shopId") shopId: String, @Param("status") status: String): List<ShopGoodsDeposit>
}
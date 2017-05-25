package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopDeposit
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopDepositRepository : BaseRepository<ShopDeposit,String> {

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_deposit t1
    WHERE
        t1.id IN (
            SELECT
                Max(id)
            FROM
                crm_shop_deposit
            WHERE
                `type` = :type
                AND shop_id IN :shopIds
            GROUP BY
                shop_id,
                type
        )
    """, nativeQuery = true)
    fun findByTypeAndShopIds(@Param("type") type: String, @Param("shopIds") shopIds: List<String>): List<ShopDeposit>

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_deposit t1
    WHERE
        t1.shop_id = :shopId
    AND t1.type = :type
    AND t1.enabled = 1
    ORDER BY
        t1.created_date DESC,
        t1.id DESC
    LIMIT 0,
     :size
    """, nativeQuery = true)
    fun findByTypeAndShopId(@Param("shopId") shopId: String, @Param("type") type: String, @Param("size") size: Int?): List<ShopDeposit>
}
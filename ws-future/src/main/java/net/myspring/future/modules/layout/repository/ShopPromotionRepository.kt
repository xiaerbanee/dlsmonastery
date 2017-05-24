package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopPromotion
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopPromotionRepository : BaseRepository<ShopPromotion,String> {

    @Query("""
    SELECT
        MAX(t1.business_id)
    FROM
        crm_shop_promotion t1
    WHERE
        t1.created_date >= ?1
    """, nativeQuery = true)
    fun findMaxBusinessId(localDate: LocalDate): String
}
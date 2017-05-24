package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAllot
import net.myspring.future.modules.layout.dto.ShopAllotDto
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAllotRepository : BaseRepository<ShopAllot,String> {

    @Query("""
    select
        max(t.business_id)
    from
        crm_shop_allot t where  t.created_date >= ?1
    """, nativeQuery = true)
    fun findMaxBusinessId(localDate: LocalDate): String

    @Query("""
    SELECT
        sum(t2.qty * t2.sale_price) saleTotalPrice,
        sum(t2.qty * t2.return_price) returnTotalPrice,
        t1.*
    FROM
        crm_shop_allot t1,
        crm_shop_allot_detail t2
    WHERE
        t1.enabled = 1
    AND t1.id = ?1
    AND t1.id = t2.shop_allot_id
    GROUP BY
        t1.id
    """, nativeQuery = true)
    fun findShopAllotDto(id: String): ShopAllotDto
}
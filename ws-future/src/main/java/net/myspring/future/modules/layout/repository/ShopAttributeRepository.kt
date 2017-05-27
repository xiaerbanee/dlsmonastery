package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAttribute
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAttributeRepository : BaseRepository<ShopAttribute,String> {

    @Query("""
    SELECT
        t1
    FROM
        #{#entityName} t1
    WHERE
        t1.shopId = ?1
    AND t1.enabled = 1
    """)
    fun findByShopId(shopId: String): MutableList<ShopAttribute>
}
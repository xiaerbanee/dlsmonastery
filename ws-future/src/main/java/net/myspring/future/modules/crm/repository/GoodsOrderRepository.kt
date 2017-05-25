package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import java.time.LocalDate

import net.myspring.future.modules.crm.domain.GoodsOrder
import org.springframework.data.jpa.repository.Query


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String> {

    @Query("""
    SELECT
        MAX(t1.business_id)
    FROM  crm_goods_order t1
    WHERE
        t1.bill_date >= ?1
        """, nativeQuery = true)
    fun findMaxBusinessId(date: LocalDate): String

}

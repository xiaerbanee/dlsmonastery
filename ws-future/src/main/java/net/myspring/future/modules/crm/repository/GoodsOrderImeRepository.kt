package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import java.time.LocalDate

import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.domain.GoodsOrderIme
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query


interface GoodsOrderImeRepository : BaseRepository<GoodsOrderIme, String> {


    fun findByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderIme>

    @Query("""
    SELECT
        t2.ime productImeIme,
        t2.meid productImeMeid,
        t1.*
    FROM
        crm_goods_order_ime t1,
        crm_product_ime t2
    WHERE
        t1.goods_order_id = ?1
    AND t1.enabled = 1
    AND t1.product_ime_id = t2.id
        """, nativeQuery = true)
    fun findDtoListByGoodsOrderId(goodsOrderId: String): MutableList<GoodsOrderImeDto>

    interface GoodsOrderImeRepository : BaseRepository<GoodsOrderIme, String>

}

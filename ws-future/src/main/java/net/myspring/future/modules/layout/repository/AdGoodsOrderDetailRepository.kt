package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdGoodsOrderDetailRepository : BaseRepository<AdGoodsOrderDetail,String> {

    fun deleteById(adGoodsOrderId: String)

    @Query("""
    SELECT
        t1.*
    FROM
        crm_ad_goods_order_detail t1
    WHERE
        t1.ad_goods_order_id IN ?1
    """, nativeQuery = true)
    fun findByAdGoodsOrderIds(adGoodsOrderIds: MutableList<String>): MutableList<AdGoodsOrderDetailDto>

    @Query("""
    SELECT
        validProduct.id productId,
        validProduct. NAME productName,
        validProduct. CODE productCode,
        validProduct.price2 price2,
        validProduct.remarks productRemarks,
        detail.*
    FROM
        (
            SELECT
                t1.*
            FROM
                crm_product t1
            WHERE
                t1.id IN (
                    SELECT
                        t3.id
                    FROM
                        crm_product t3
                    WHERE
                        t3.out_group_id IN :outGroupIdList
                    AND t1.enabled = 1
                ) validProduct
            LEFT JOIN crm_ad_goods_order_detail detail ON validProduct.id = detail.product_id
            AND detail.ad_goods_order_id = :adGoodsOrderId
            ORDER BY
                detail.qty DESC
    """, nativeQuery = true)
    fun findByAdGoodsOrderForEdit(@Param("outGroupIdList") outGroupIdList: MutableList<String>, @Param("adGoodsOrderId") adGoodsOrderId: String): MutableList<AdGoodsOrderDetailDto>

    @Query("""
    SELECT
        t1.id productId,
        t1. NAME productName,
        t1. CODE productCode,
        t1.price2 price2,
        t1.remarks productRemarks
    FROM
        crm_product t1
    WHERE
        t1.out_group_id IN :outGroupIdList
    AND t1.enabled = 1
    """, nativeQuery = true)
    fun findByAdGoodsOrderForNew(@Param("outGroupIdList") outGroupIdList: MutableList<String>): MutableList<AdGoodsOrderDetailDto>
}
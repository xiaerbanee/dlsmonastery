package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.HashMap


interface AdGoodsOrderDetailRepository : BaseRepository<AdGoodsOrderDetail,String> ,AdGoodsOrderDetailRepositoryCustom{

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

    fun findDtoListByAdGoodsOrderId(adGoodsOrderId :String): MutableList<AdGoodsOrderDetailDto>

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

interface AdGoodsOrderDetailRepositoryCustom{

    fun findDetailListForNew(companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto>

    fun findDetailListForEdit(adGoodsOrderId: String, companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto>

}

class AdGoodsOrderDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdGoodsOrderDetailRepositoryCustom{
    override fun findDetailListForEdit(adGoodsOrderId: String, companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto> {
        val paramMap = HashMap<String, Any>()
        paramMap.put("adGoodsOrderId",adGoodsOrderId)
        paramMap.put("companyId",companyId)
        paramMap.put("outGroupIdList",outGroupIdList)
        paramMap.put("includeNotAllowOrderProduct",includeNotAllowOrderProduct)

        return namedParameterJdbcTemplate.query("""
        SELECT
            result.id,
            result.productId,
            result.productCode,
            result.productName,
            result.productPrice2,
            result.productRemarks,
            result.qty
        FROM
            (
                (
                    SELECT
                        t1.id id,
                        t1.product_id productId,
                        t2.code productCode,
                        t2.name productName,
                        t2.price2 productPrice2,
                        t2.remarks productRemarks,
                        t1.qty qty
                    FROM
                        crm_ad_goods_order_detail t1,
                        crm_product t2
                    WHERE
                        t1.ad_goods_order_id = :adGoodsOrderId
                    AND t1.product_id = t2.id
                )
                UNION ALL
                    (
                        SELECT
                            NULL id,
                            t1.id productId,
                            t1.code productCode,
                            t1.name productName,
                            t1.price2 productPrice2,
                            t1.remarks productRemarks,
                            NULL qty
                        FROM
                            crm_product t1
                        WHERE
                            t1.enabled = 1
                        AND t1.company_id = :companyId
                        AND t1.out_group_id IN (:outGroupIdList)
                        AND ( :includeNotAllowOrderProduct OR t1.allow_order = 1)
                        AND NOT EXISTS (
                            SELECT *
                            FROM crm_ad_goods_order_detail detail
                            WHERE detail.ad_goods_order_id = :adGoodsOrderId AND detail.product_id = t1.id
                        )
                    )
            ) result
        ORDER BY
            result.qty DESC
          """, paramMap, BeanPropertyRowMapper(AdGoodsOrderDetailDto::class.java))

    }

    override fun findDetailListForNew(companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto> {
        val paramMap = HashMap<String, Any>()
        paramMap.put("companyId",companyId)
        paramMap.put("outGroupIdList",outGroupIdList)
        paramMap.put("includeNotAllowOrderProduct",includeNotAllowOrderProduct)

        return namedParameterJdbcTemplate.query("""
        SELECT
          NULL id,
          t1.id productId,
          t1.code productCode,
          t1.name productName,
          t1.price2 productPrice2,
          t1.remarks productRemarks,
          NULL qty
        FROM
          crm_product t1
        WHERE
          t1.enabled = 1
          AND t1.company_id = :companyId
          AND t1.out_group_id IN (:outGroupIdList)
          AND ( :includeNotAllowOrderProduct OR t1.allow_order = 1)


          """, paramMap, BeanPropertyRowMapper(AdGoodsOrderDetailDto::class.java))
    }

}

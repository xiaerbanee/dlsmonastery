package net.myspring.future.modules.layout.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailExportDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface AdGoodsOrderDetailRepository : BaseRepository<AdGoodsOrderDetail,String> ,AdGoodsOrderDetailRepositoryCustom{

    fun findByAdGoodsOrderId(adGoodsOrderId :String): MutableList<AdGoodsOrderDetail>

}

interface AdGoodsOrderDetailRepositoryCustom{

    fun findDetailListForNew(companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto>

    fun findDetailListForEdit(adGoodsOrderId: String, companyId:String, outGroupIdList:List<String>, includeNotAllowOrderProduct : Boolean): List<AdGoodsOrderDetailDto>

    fun findDtoListByAdGoodsOrderId(adGoodsOrderId :String): MutableList<AdGoodsOrderDetailDto>

    fun findDetailListForBill(adGoodsOrderId: String, companyId: String, outGroupIdList: List<String>): List<AdGoodsOrderDetailDto>

    fun findDtoListForExport(adGoodsOrderIdList: List<String>, limit: Int): List<AdGoodsOrderDetailExportDto>
}

class AdGoodsOrderDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdGoodsOrderDetailRepositoryCustom{

    override fun findDtoListForExport(adGoodsOrderIdList: List<String>, limit: Int): List<AdGoodsOrderDetailExportDto> {

        val params = HashMap<String, Any>()
        params.put("adGoodsOrderIdList", adGoodsOrderIdList)
        params.put("limit", limit)
        return namedParameterJdbcTemplate.query("""
        SELECT t2.out_code adGoodsOrderOutCode,
                    t2.out_shop_id adGoodsOrderOutShopId,
                    t2.id adGoodsOrderId,
                    t2.parent_id adGoodsOrderParentId,
                    t2.shop_id adGoodsOrderShopId,
                    t2.bill_address adGoodsOrderBillAddress,
                    t2.employee_id adGoodsOrderEmployeeId,
                    t2.bill_remarks adGoodsOrderBillRemarks,
                    t2.process_status adGoodsOrderProcessStatus,
                    t2.created_by adGoodsOrderCreatedBy,
                    t2.created_date adGoodsOrderCreatedDate,
                    t2.bill_date adGoodsOrderBillDate,
                    t2.bill_remarks adGoodsOrderBillRemarks,
                    t2.parent_id adGoodsOrderParentId,
                    t4.area_type adGoodsOrderDepotShopAreaType,
                    t5.contator expressOrderContator,
                    t5.mobile_phone expressOrderMobilePhone,
                    t5.express_company_id expressOrderExpressCompanyId,
                    t5.should_pay expressOrderShouldPay,
                    t5.should_get expressOrderShouldGet,
                    t5.real_pay expressOrderRealPay,
                    t6.price2 productPrice2,
                    t6.code productCode,
                    t6.name productName,
                    t6.volume productVolume,
                    t1.*
        FROM  crm_ad_goods_order_detail t1
                  LEFT JOIN crm_ad_goods_order t2 ON t1.ad_goods_order_id = t2.id
                  LEFT JOIN crm_depot t3 ON t2.shop_id = t3.id
                  LEFT JOIN crm_depot_shop t4 ON t3.depot_shop_id = t4.id
                  LEFT JOIN crm_express_order t5 ON t2.express_order_id = t5.id
                  LEFT JOIN crm_product t6 ON t1.product_id = t6.id
        where t1.ad_goods_order_id IN (:adGoodsOrderIdList)
        ORDER BY t1.ad_goods_order_id DESC
        limit 0, :limit
          """, params, MyBeanPropertyRowMapper(AdGoodsOrderDetailExportDto::class.java))
    }


    override fun findDetailListForBill(adGoodsOrderId: String, companyId: String, outGroupIdList: List<String>): List<AdGoodsOrderDetailDto> {

        val paramMap = HashMap<String, Any>()
        paramMap.put("adGoodsOrderId",adGoodsOrderId)
        paramMap.put("companyId",companyId)
        paramMap.put("outGroupIdList",outGroupIdList)

        return namedParameterJdbcTemplate.query("""
        SELECT
            result.id,
            result.productId,
            result.productCode,
            result.productName,
            result.productPrice2,
            result.productRemarks,
            result.qty,
            result.confirmQty,
            result.billQty
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
                        t1.qty qty,
                        t1.confirm_qty confirmQty,
                        t1.bill_qty billQty
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
                            NULL qty,
                            NULL confirmQty,
                            NULL billQty
                        FROM
                            crm_product t1
                        WHERE
                            t1.enabled = 1
                        AND t1.company_id = :companyId
                        AND t1.out_group_id IN (:outGroupIdList)
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

    override fun findDtoListByAdGoodsOrderId(adGoodsOrderId: String): MutableList<AdGoodsOrderDetailDto> {
        return namedParameterJdbcTemplate.query("""
                    SELECT
                        t2.code productCode,
                        t2.name productName,
                        t2.price2 productPrice2,
                        t2.remarks productRemarks,
                        t1.*
                    FROM
                        crm_ad_goods_order_detail t1,
                        crm_product t2
                    WHERE
                        t1.ad_goods_order_id = :adGoodsOrderId
                    AND t1.product_id = t2.id
          """, Collections.singletonMap("adGoodsOrderId", adGoodsOrderId), BeanPropertyRowMapper(AdGoodsOrderDetailDto::class.java))
    }

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

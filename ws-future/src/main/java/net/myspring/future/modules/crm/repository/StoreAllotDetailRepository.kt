package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.StoreAllotDetail
import net.myspring.future.modules.crm.dto.StoreAllotDetailSimpleDto
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface StoreAllotDetailRepository : BaseRepository<StoreAllotDetail, String> ,StoreAllotDetailRepositoryCustom{
}


interface StoreAllotDetailRepositoryCustom{

    fun findStoreAllotDetailsForFastAllot(billDate: LocalDate, toStoreId: String, status: String,  companyId: String): MutableList<StoreAllotDetailSimpleDto>

    fun findByStoreAllotIds(storeAllotIdList: MutableList<String>): MutableList<StoreAllotDetailDto>

    fun findStoreAllotDetailListForNew(companyId: String): MutableList<StoreAllotDetailSimpleDto>

}

class StoreAllotDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): StoreAllotDetailRepositoryCustom{
    override fun findStoreAllotDetailListForNew(companyId: String): MutableList<StoreAllotDetailSimpleDto> {
        return namedParameterJdbcTemplate.query("""
         SELECT
            t1.id productId
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
            AND t1.company_id = :companyId
          """, Collections.singletonMap("companyId", companyId), MyBeanPropertyRowMapper(StoreAllotDetailSimpleDto::class.java))
    }

    override fun findByStoreAllotIds(storeAllotIdList: MutableList<String>): MutableList<StoreAllotDetailDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT product.name productName, product.out_id outId, product.has_ime productHasIme, 0 shipQty, t1.*
        FROM  crm_store_allot_detail t1, crm_product product
        WHERE t1.product_id = product.id
            AND  t1.store_allot_id in (:storeAllotIdList)
            ORDER BY t1.store_allot_id, product.has_ime DESC
          """, Collections.singletonMap("storeAllotIdList", storeAllotIdList), MyBeanPropertyRowMapper(StoreAllotDetailDto::class.java))
    }

    override fun findStoreAllotDetailsForFastAllot(billDate: LocalDate, toStoreId: String, status: String, companyId: String): MutableList<StoreAllotDetailSimpleDto> {
        val params = HashMap<String, Any>()
        params.put("billDate", billDate)
        params.put("toStoreId", toStoreId)
        params.put("status", status)
        params.put("companyId", companyId)

        return namedParameterJdbcTemplate.query("""

        SELECT result.productId productId, SUM(result.billQty) billQty
        FROM
            (
                ( SELECT detail.product_id productId, SUM(detail.bill_qty) billQty
                  FROM  crm_goods_order_detail detail
                  WHERE detail.goods_order_id IN ( SELECT t1.id FROM  crm_goods_order t1 WHERE t1.bill_date = :billDate AND t1.store_id = :toStoreId AND t1. STATUS = :status AND t1.enabled = 1 )
                  GROUP BY detail.product_id  )
               UNION ALL
                ( SELECT  ime.product_id productId, COUNT(*) billQty
                  FROM crm_goods_order_ime ime
                  WHERE  ime.goods_order_id IN ( SELECT t2.id FROM  crm_goods_order t2 WHERE t2.bill_date = :billDate AND t2.store_id = :toStoreId AND t2. STATUS = :status AND t2.enabled = 1 )
                  GROUP BY ime.product_id )
                UNION ALL
                ( SELECT  t1.id productId, 0 billQty
                  FROM crm_product t1
                  WHERE  t1.enabled=1 AND t1.company_id = :companyId )
            ) result
        GROUP BY result.productId
        ORDER BY result.billQty DESC
                """, params, MyBeanPropertyRowMapper(StoreAllotDetailSimpleDto::class.java))
    }

}
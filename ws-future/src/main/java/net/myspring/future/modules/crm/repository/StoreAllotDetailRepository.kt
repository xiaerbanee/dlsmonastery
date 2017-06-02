package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.StoreAllotDetail
import net.myspring.future.modules.crm.dto.SimpleStoreAllotDetailDto
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*


interface StoreAllotDetailRepository : BaseRepository<StoreAllotDetail, String> ,StoreAllotDetailRepositoryCustom{

    fun deleteByStoreAllotId(storeAllotId: String)

}


interface StoreAllotDetailRepositoryCustom{

    fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto>

    fun findStoreAllotDtoById(id: String): StoreAllotDto

    fun findStoreAllotDetailsForFastAllot(billDate: LocalDate, toStoreId: String, status: String,  companyId: String): MutableList<SimpleStoreAllotDetailDto>

    fun findByStoreAllotIds(storeAllotIdList: MutableList<String>): MutableList<StoreAllotDetailDto>

    fun findStoreAllotDetailListForNew(companyId: String): MutableList<SimpleStoreAllotDetailDto>

}

class StoreAllotDetailRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): StoreAllotDetailRepositoryCustom{
    override fun findStoreAllotDetailListForNew(companyId: String): MutableList<SimpleStoreAllotDetailDto> {
        return namedParameterJdbcTemplate.query("""
         SELECT
            t1.id productId
        FROM
            crm_product t1
        WHERE
            t1.enabled = 1
            AND t1.company_id = :companyId
          """, Collections.singletonMap("companyId", companyId), MyBeanPropertyRowMapper(SimpleStoreAllotDetailDto::class.java))
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

    override fun findStoreAllotDetailsForFastAllot(billDate: LocalDate, toStoreId: String, status: String, companyId: String): MutableList<SimpleStoreAllotDetailDto> {
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
                """, params, MyBeanPropertyRowMapper(SimpleStoreAllotDetailDto::class.java))
    }

    override fun findStoreAllotDtoById(id: String): StoreAllotDto {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t2.express_codes expressOrderExpressCodes,
                t2.express_company_id,
                t1.*
            FROM
                crm_store_allot t1
                LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
            WHERE
                t1.enabled = 1
                AND t1.id =  :id
                """, Collections.singletonMap("id", id), MyBeanPropertyRowMapper(StoreAllotDto::class.java))
    }

    override fun findPage(pageable: Pageable, storeAllotQuery: StoreAllotQuery): Page<StoreAllotDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
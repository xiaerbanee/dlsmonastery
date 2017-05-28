package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAllotDetail
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


interface ShopAllotDetailRepository : BaseRepository<ShopAllotDetail,String>, ShopAllotDetailRepositoryCustom{

    fun deleteByShopAllotId(shopAllotId: String)

}


interface ShopAllotDetailRepositoryCustom{
    fun getShopAllotDetailListForViewOrAudit(shopAllotId: String): MutableList<ShopAllotDetailDto>

    fun getShopAllotDetailListForEdit(shopAllotId: String, fromDepotId: String, toDepotId: String): MutableList<ShopAllotDetailDto>

    fun getShopAllotDetailListForNew(fromDepotId: String, toDepotId: String): MutableList<ShopAllotDetailDto>


}

class ShopAllotDetailRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): ShopAllotDetailRepositoryCustom {
    override fun getShopAllotDetailListForNew(fromDepotId: String, toDepotId: String): MutableList<ShopAllotDetailDto> {
        val paramMap = HashMap<String, Any>()
        paramMap.put("fromDepotId",fromDepotId)
        paramMap.put("toDepotId",toDepotId)
        return namedParameterJdbcTemplate.query("""
        SELECT
          t1.id productId,
          t1.name productName,
          NULL qty
        FROM
          crm_product t1
        WHERE
          t1.enabled = 1
          AND t1.id IN ( SELECT t2.product_id FROM crm_pricesystem_detail t2 WHERE  t2.pricesystem_id = (SELECT depot1.pricesystem_id from crm_depot depot1 where depot1.id = :fromDepotId)  )
          AND t1.id IN ( SELECT t3.product_id FROM crm_pricesystem_detail t3 WHERE  t3.pricesystem_id = (SELECT depot2.pricesystem_id from crm_depot depot2 where depot2.id = :toDepotId) )
        ORDER BY
            qty DESC
          """, paramMap, BeanPropertyRowMapper(ShopAllotDetailDto::class.java))
    }


    override fun getShopAllotDetailListForEdit(shopAllotId: String , fromDepotId: String, toDepotId: String): MutableList<ShopAllotDetailDto> {
        val paramMap = HashMap<String, Any>()
        paramMap.put("shopAllotId",shopAllotId)
        paramMap.put("fromDepotId",fromDepotId)
        paramMap.put("toDepotId",toDepotId)
        return namedParameterJdbcTemplate.query("""
       SELECT
            result.productId,
            result.productName,
            result.qty
        FROM
            (
                (
                    SELECT
                        t1.product_id productId,
                        t2.name productName,
                        t1.qty
                    FROM
                        crm_shop_allot_detail t1,
                        crm_product t2
                    WHERE
                        t1.shop_allot_id = :shopAllotId
                    AND t1.product_id = t2.id
                )
                UNION ALL
                    (
                        SELECT
                            t1.id productId,
                            t1.name productName,
                            NULL qty
                        FROM
                            crm_product t1
                        WHERE
                            t1.enabled = 1
                            AND t1.id IN ( SELECT t2.product_id FROM crm_pricesystem_detail t2 WHERE  t2.pricesystem_id = (SELECT depot1.pricesystem_id from crm_depot depot1 where depot1.id = :fromDepotId)  )
                            AND t1.id IN ( SELECT t3.product_id FROM crm_pricesystem_detail t3 WHERE  t3.pricesystem_id = (SELECT depot2.pricesystem_id from crm_depot depot2 where depot2.id = :toDepotId) )
                            AND NOT EXISTS (
                                SELECT  *
                                FROM crm_shop_allot_detail detail
                                WHERE detail.shop_allot_id = :shopAllotId AND detail.product_id = t1.id
                            )
                    )
            ) result
        ORDER BY
            result.qty DESC
          """, paramMap, BeanPropertyRowMapper(ShopAllotDetailDto::class.java))
    }

    override fun getShopAllotDetailListForViewOrAudit(shopAllotId: String): MutableList<ShopAllotDetailDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.id,
            t1.product_id,
            t1.shop_allot_id,
            t1.qty,
            t1.return_price,
            t1.sale_price,
            t2.name productName
        FROM
            crm_shop_allot_detail t1,
            crm_product t2
        WHERE
            t1.product_id = t2.id
        AND t1.shop_allot_id = :shopAllotId
          """, Collections.singletonMap("shopAllotId", shopAllotId), BeanPropertyRowMapper(ShopAllotDetailDto::class.java))
    }

}


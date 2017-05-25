package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAllotDetail
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAllotDetailRepository : BaseRepository<ShopAllotDetail,String>{

    fun findByShopAllotId(shopAllotId: String): List<ShopAllotDetail>


    @Query("""
    DELETE
    FROM
        crm_shop_allot_detail
    WHERE
        shop_allot_id = ?1
    """, nativeQuery = true)
    fun deleteByShopAllotId(shopAllotId: String): Int

    @Query("""
    SELECT
        result.productId,
        result.productName,
        result.qty
    FROM
	(
		(
			SELECT
				t1.product_id productId,
				t2. NAME productName,
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
				t1. NAME productName,
				NULL qty
			FROM
				crm_product t1
			WHERE
				t1.enabled = 1
                AND t1.id IN ( SELECT t2.product_id FROM crm_pricesystem_detail t2 WHERE t2.pricesystem_id = ( SELECT depot1.pricesystem_id FROM crm_depot depot1 WHERE depot1.id = :fromDepotId) )
                AND t1.id IN ( SELECT t3.product_id FROM crm_pricesystem_detail t3 WHERE t3.pricesystem_id = ( SELECT depot2.pricesystem_id FROM crm_depot depot2 WHERE depot2.id = :toDepotId) )
                AND NOT EXISTS ( SELECT detail.* FROM crm_shop_allot_detail detail WHERE detail.shop_allot_id = :shopAllotId AND detail.product_id = t1.id)
        )
    ) result
    ORDER BY result.qty DESC
    """, nativeQuery = true)
    fun getShopAllotDetailListForNewOrEdit(@Param("shopAllotId") shopAllotId: String, @Param("fromDepotId") fromDepotId: String, @Param("toDepotId") toDepotId: String): List<ShopAllotDetailDto>

    @Query("""
    SELECT
        t1.id,
        t1.product_id,
        t1.shop_allot_id,
        t1.qty,
        t1.return_price,
        t1.sale_price,
        t2. NAME productName
    FROM
        crm_shop_allot_detail t1,
        crm_product t2
    WHERE
        t1.product_id = t2.id
    AND t1.shop_allot_id = ?1
    """, nativeQuery = true)
    fun getShopAllotDetailListForViewOrAudit(shopAllotId: String): List<ShopAllotDetailDto>
}

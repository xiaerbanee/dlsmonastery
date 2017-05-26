package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopPromotion
import net.myspring.future.modules.layout.dto.ShopPrintDto
import net.myspring.future.modules.layout.dto.ShopPromotionDto
import net.myspring.future.modules.layout.web.query.ShopPrintQuery
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopPromotionRepository : BaseRepository<ShopPromotion,String>,ShopPromotionRepositoryCustom {

    @Query("""
    SELECT
        MAX(t1.business_id)
    FROM
        crm_shop_promotion t1
    WHERE
        t1.created_date >= ?1
    """, nativeQuery = true)
    fun findMaxBusinessId(localDate: LocalDate): String
}

interface ShopPromotionRepositoryCustom{
    fun findPage(pageable: Pageable, shopPromotionQuery: ShopPromotionQuery): Page<ShopPromotionDto>
}

class ShopPromotionRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopPromotionRepositoryCustom{

    override fun findPage(pageable: Pageable, shopPromotionQuery: ShopPromotionQuery): Page<ShopPromotionDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_shop_promotion t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(shopPromotionQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopPromotionQuery.businessId)) {
            sb.append("""  and t1.business_id LIKE CONCAT('%',#{p.businessId},'%') """)
        }
        if (StringUtils.isNotEmpty(shopPromotionQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopPromotionQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }

        var query = entityManager.createNativeQuery(sb.toString(), ShopPromotionDto::class.java)

        return query.resultList as Page<ShopPromotionDto>
    }
}
package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopDeposit
import net.myspring.future.modules.layout.dto.ShopDepositDto
import net.myspring.future.modules.layout.web.query.ShopDepositQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopDepositRepository : BaseRepository<ShopDeposit,String>,ShopDepositRepositoryCustom {

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_deposit t1
    WHERE
        t1.id IN (
            SELECT
                Max(id)
            FROM
                crm_shop_deposit
            WHERE
                `type` = :type
                AND shop_id IN :shopIds
            GROUP BY
                shop_id,
                type
        )
    """, nativeQuery = true)
    fun findByTypeAndShopIds(@Param("type") type: String, @Param("shopIds") shopIds: MutableList<String>): MutableList<ShopDeposit>

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_deposit t1
    WHERE
        t1.shop_id = :shopId
    AND t1.type = :type
    AND t1.enabled = 1
    ORDER BY
        t1.created_date DESC,
        t1.id DESC
    LIMIT 0,
     :size
    """, nativeQuery = true)
    fun findByTypeAndShopId(@Param("shopId") shopId: String, @Param("type") type: String, @Param("size") size: Int?): MutableList<ShopDeposit>
}

interface ShopDepositRepositoryCustom{
    fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto>
}

class ShopDepositRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopDepositRepositoryCustom{

    override fun findPage(pageable: Pageable, shopDepositQuery: ShopDepositQuery): Page<ShopDepositDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopDepositDto::class.java)

        return query.resultList as Page<ShopDepositDto>
    }
}
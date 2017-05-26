package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopGoodsDepositRepository : BaseRepository<ShopGoodsDeposit,String>,ShopGoodsDepositRepositoryCustom {

    @Query("""
    SELECT
        t1.*
    FROM
        crm_shop_goods_deposit t1
    WHERE
        t1.enabled = 1
    AND t1.shop_id = :shopId
    AND t1.status = :status
    ORDER BY
        created_date DESC
    """, nativeQuery = true)
    fun findByShopId(@Param("shopId") shopId: String, @Param("status") status: String): MutableList<ShopGoodsDeposit>
}

interface ShopGoodsDepositRepositoryCustom{
    fun findPage(pageable: Pageable, shopGoodsDepositQuery: ShopGoodsDepositQuery): Page<ShopGoodsDepositDto>
}

class ShopGoodsDepositRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopGoodsDepositRepositoryCustom{

    override fun findPage(pageable: Pageable, shopGoodsDepositQuery: ShopGoodsDepositQuery): Page<ShopGoodsDepositDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopGoodsDepositDto::class.java)

        return query.resultList as Page<ShopGoodsDepositDto>
    }
}
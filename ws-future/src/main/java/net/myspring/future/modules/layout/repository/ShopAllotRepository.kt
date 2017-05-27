package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAllot
import net.myspring.future.modules.layout.dto.ShopAllotDto
import net.myspring.future.modules.layout.web.query.ShopAllotQuery
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
interface ShopAllotRepository : BaseRepository<ShopAllot,String>,ShopAllotRepositoryCustom{

    @Query("""
    select
        max(t.businessId)
    from
        #{#entityName} t
    where  t.createdDate >= ?1
    """)
    fun findMaxBusinessId(localDate: LocalDate): String

    @Query("""
    SELECT
        sum(t2.qty * t2.sale_price) saleTotalPrice,
        sum(t2.qty * t2.return_price) returnTotalPrice,
        t1.*
    FROM
        crm_shop_allot t1,
        crm_shop_allot_detail t2
    WHERE
        t1.enabled = 1
    AND t1.id = ?1
    AND t1.id = t2.shop_allot_id
    GROUP BY
        t1.id
    """, nativeQuery = true)
    fun findShopAllotDto(id: String): ShopAllotDto
}

interface ShopAllotRepositoryCustom{
    fun findPage(pageable: Pageable, shopAllotQuery: ShopAllotQuery): Page<ShopAllotDto>
}

class ShopAllotRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopAllotRepositoryCustom{

    override fun findPage(pageable: Pageable, shopAllotQuery: ShopAllotQuery): Page<ShopAllotDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*
            FROM
                crm_shop_allot t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(shopAllotQuery.fromShopId)) {
            sb.append("""  and t1.from_shop_id = :fromShopId """)
        }
        if (StringUtils.isNotEmpty(shopAllotQuery.toShopId)) {
            sb.append("""  and t1.to_shop_id = :toShopId """)
        }
        if (StringUtils.isNotEmpty(shopAllotQuery.status)) {
            sb.append("""  and t1.status = :status """)
        }
        if (StringUtils.isNotEmpty(shopAllotQuery.businessId)) {
            sb.append("""  and t1.business_id like concat('%',:businessId,'%') """)
        }
        if (shopAllotQuery.createdDateStart != null) {
            sb.append("""  and t1.created_data >= :createdDateStart """)
        }
        if (shopAllotQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_data < :createdDateEnd """)
        }
        if (shopAllotQuery.auditDateStart != null) {
            sb.append("""  and t1.audit_date >= :auditDateStart """)
        }
        if (shopAllotQuery.auditDateEnd != null) {
            sb.append("""  and t1.audit_date < :auditDateEnd """)
        }

        var query = entityManager.createNativeQuery(sb.toString(), ShopAllotDto::class.java)

        return query.resultList as Page<ShopAllotDto>
    }
}
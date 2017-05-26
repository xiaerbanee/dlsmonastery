package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotShop
import net.myspring.future.modules.basic.dto.DepotShopDto
import net.myspring.future.modules.basic.web.query.DepotQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface DepotShopRepository : BaseRepository<DepotShop,String>,DepotShopRepositoryCustom {
}

interface DepotShopRepositoryCustom{
    fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotShopDto>
}

class DepotShopRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DepotShopRepositoryCustom{

    override fun findPage(pageable: Pageable, depotShopQuery: DepotQuery): Page<DepotShopDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.id AS 'depotId',
                t2.*
            FROM
                crm_depot t1,
                crm_depot_shop t2
            WHERE
                t1.enabled = 1
            AND t2.enabled = 1
            AND t1.depot_shop_id = t2.id
        """)
        if (StringUtils.isNotEmpty(depotShopQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), DepotShopDto::class.java)

        return query.resultList as Page<DepotShopDto>
    }
}
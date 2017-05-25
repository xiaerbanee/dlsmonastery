package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotShop
import net.myspring.future.modules.basic.dto.DepotShopDto
import net.myspring.future.modules.basic.web.query.DepotQuery
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

    override fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotShopDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotShopDto::class.java)

        return query.resultList as Page<DepotShopDto>
    }
}
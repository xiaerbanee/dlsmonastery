package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopImage
import net.myspring.future.modules.layout.dto.ShopImageDto
import net.myspring.future.modules.layout.web.query.ShopImageQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopImageRepository : BaseRepository<ShopImage,String>,ShopImageRepositoryCustom {
}

interface ShopImageRepositoryCustom{
    fun findPage(pageable: Pageable, shopImageQuery: ShopImageQuery): Page<ShopImageDto>
}

class ShopImageRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopImageRepositoryCustom{

    override fun findPage(pageable: Pageable, shopImageQuery: ShopImageQuery): Page<ShopImageDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopImageDto::class.java)

        return query.resultList as Page<ShopImageDto>
    }
}
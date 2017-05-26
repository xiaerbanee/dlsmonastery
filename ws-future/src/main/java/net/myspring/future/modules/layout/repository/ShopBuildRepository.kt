package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopBuild
import net.myspring.future.modules.layout.dto.ShopAllotDto
import net.myspring.future.modules.layout.dto.ShopBuildDto
import net.myspring.future.modules.layout.web.query.ShopAllotQuery
import net.myspring.future.modules.layout.web.query.ShopBuildQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopBuildRepository : BaseRepository<ShopBuild,String>,ShopBuildRepositoryCustom {

}

interface ShopBuildRepositoryCustom{
    fun findPage(pageable: Pageable, shopBuildQuery: ShopBuildQuery): Page<ShopBuildDto>

    fun findByFilter(shopBuildQuery: ShopBuildQuery): MutableList<ShopBuildDto>
}

class ShopBuildRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopBuildRepositoryCustom{

    override fun findPage(pageable: Pageable, shopBuildQuery: ShopBuildQuery): Page<ShopBuildDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopBuildDto::class.java)

        return query.resultList as Page<ShopBuildDto>
    }

    override fun findByFilter(shopBuildQuery: ShopBuildQuery): MutableList<ShopBuildDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopBuildDto::class.java)

        return query.resultList as MutableList<ShopBuildDto>
    }
}
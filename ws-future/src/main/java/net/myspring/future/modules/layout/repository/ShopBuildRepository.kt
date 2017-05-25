package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopBuild
import net.myspring.future.modules.layout.dto.ShopBuildDto
import net.myspring.future.modules.layout.web.query.ShopBuildQuery
import org.springframework.data.repository.query.Param

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopBuildRepository : BaseRepository<ShopBuild,String> {

    fun findByFilter(@Param("p") shopBuildQuery: ShopBuildQuery): List<ShopBuildDto>
}
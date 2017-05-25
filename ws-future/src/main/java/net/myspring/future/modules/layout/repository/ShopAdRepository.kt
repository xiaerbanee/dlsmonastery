package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAd
import net.myspring.future.modules.layout.dto.ShopAdDto
import net.myspring.future.modules.layout.web.query.ShopAdQuery
import org.springframework.data.repository.query.Param

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAdRepository : BaseRepository<ShopAd,String> {


    fun findByFilter(@Param("p") shopAdQuery: ShopAdQuery): List<ShopAdDto>
}
package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopPrint
import net.myspring.future.modules.layout.dto.ShopPrintDto
import net.myspring.future.modules.layout.web.query.ShopPrintQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopPrintRepository : BaseRepository<ShopPrint,String>,ShopPrintRepositoryCustom {

}

interface ShopPrintRepositoryCustom{
    fun findPage(pageable: Pageable, shopPrintQuery: ShopPrintQuery): Page<ShopPrintDto>
}

class ShopPrintRepositoryImpl @Autowired constructor(val entityManager: EntityManager):ShopPrintRepositoryCustom{

    override fun findPage(pageable: Pageable, shopPrintQuery: ShopPrintQuery): Page<ShopPrintDto> {
        val sb = StringBuffer()
        var query = entityManager.createNativeQuery(sb.toString(), ShopPrintDto::class.java)

        return query.resultList as Page<ShopPrintDto>
    }
}
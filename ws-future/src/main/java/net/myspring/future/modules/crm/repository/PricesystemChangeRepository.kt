package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import net.myspring.future.modules.crm.domain.PricesystemChange
import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.dto.PricesystemChangeDto
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager


interface PricesystemChangeRepository : BaseRepository<PricesystemChange, String>, PricesystemChangeRepositoryCustom {




}


interface PricesystemChangeRepositoryCustom{
    fun findPage(pageable : Pageable, pricesystemChangeQuery : PricesystemChangeQuery): Page<PricesystemChangeDto>?

}

class PricesystemChangeRepositoryImpl @Autowired constructor(val entityManager: EntityManager): PricesystemChangeRepositoryCustom {
    override fun findPage(pageable : Pageable, pricesystemChangeQuery: PricesystemChangeQuery): Page<PricesystemChangeDto>? {

        return null

    }


}
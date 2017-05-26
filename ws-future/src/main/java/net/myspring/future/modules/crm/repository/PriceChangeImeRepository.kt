package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.*

import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeImeDto
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager


interface PriceChangeImeRepository : BaseRepository<PriceChangeIme, String>,PriceChangeImeRepositoryCustom {

    fun findByPriceChangeId(priceChangeId: String): MutableList<PriceChangeIme>



    fun findByPriceChangeIdAndUploadDateIsNotNullAndEnabledIsTrue(priceChangeId: String): MutableList<PriceChangeIme>




}


interface PriceChangeImeRepositoryCustom{
    fun findPage(pageable : Pageable, priceChangeImeQuery : PriceChangeImeQuery): Page<PriceChangeImeDto>?

}

class PriceChangeImeRepositoryImpl @Autowired constructor(val entityManager: EntityManager): PriceChangeImeRepositoryCustom {
    override fun findPage(pageable : Pageable, priceChangeImeQuery: PriceChangeImeQuery): Page<PriceChangeImeDto>? {

        return null

    }


}
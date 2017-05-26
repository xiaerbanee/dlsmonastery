package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository

import java.time.LocalDate

import net.myspring.future.modules.crm.domain.GoodsOrder
import net.myspring.future.modules.crm.dto.ExpressDto
import net.myspring.future.modules.crm.dto.GoodsOrderDto
import net.myspring.future.modules.crm.web.query.ExpressQuery
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


interface GoodsOrderRepository : BaseRepository<GoodsOrder, String> , GoodsOrderRepositoryCustom {

    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM  #{#entityName} t1
    WHERE
        t1.billDate >= ?1
        """)
    fun findMaxBusinessId(date: LocalDate): String

}

interface GoodsOrderRepositoryCustom{
    fun findPage(pageable : Pageable, goodsOrderQuery : GoodsOrderQuery): Page<GoodsOrderDto>?

}

class GoodsOrderRepositoryImpl @Autowired constructor(val entityManager: EntityManager): GoodsOrderRepositoryCustom {
    override fun findPage(pageable : Pageable, goodsOrderQuery: GoodsOrderQuery): Page<GoodsOrderDto>? {

        return null

    }


}
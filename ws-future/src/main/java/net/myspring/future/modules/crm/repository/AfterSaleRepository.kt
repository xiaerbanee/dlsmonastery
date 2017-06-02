package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import javax.persistence.EntityManager


interface AfterSaleRepository : BaseRepository<AfterSale, String>,AfterSaleRepositoryCustom {

    @Query("""
    SELECT t1
    FROM #{#entityName} t1, ProductIme t2
    WHERE t1.badProductImeId=t2.id
        AND t2.ime in ?1
        """)
    fun findByBadProductImeIn(imeList: MutableList<String>): MutableList<AfterSale>

    @Query("""
    SELECT MAX(t1.businessId)
    FROM #{#entityName} t1
    WHERE t1.createdDate >= ?1
        """)
    fun findMaxBusinessId(dateStart: LocalDate): String

}


interface AfterSaleRepositoryCustom{
    fun findPage(pageable: Pageable, afterSaleQuery : AfterSaleQuery): Page<AfterSale>?
}

class AfterSaleRepositoryImpl @Autowired constructor(val entityManager: EntityManager): AfterSaleRepositoryCustom {
    override fun findPage(pageable: Pageable, afterSaleQuery: AfterSaleQuery): Page<AfterSale>? {
        return null


    }


}
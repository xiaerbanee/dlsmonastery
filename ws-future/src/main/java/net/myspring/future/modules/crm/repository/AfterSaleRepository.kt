package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.AfterSale
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime


interface AfterSaleRepository : BaseRepository<AfterSale, String> {

    @Query("""
    SELECT t1.*
    FROM crm_after_sale t1,crm_product_ime t2
    WHERE t1.bad_product_ime_id=t2.id
        AND t2.ime in ?1
        """, nativeQuery = true)
    fun findByBadProductImeIn(imeList: List<String>): List<AfterSale>

    @Query("""
    SELECT MAX(t1.business_id)
    FROM crm_after_sale t1
    WHERE t1.created_date >= ?1
        """, nativeQuery = true)
    fun findMaxBusinessId(dateStart: LocalDate): String

}

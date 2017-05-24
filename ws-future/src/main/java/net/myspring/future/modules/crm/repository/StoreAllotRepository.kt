package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import net.myspring.future.modules.crm.web.query.StoreAllotQuery
import net.myspring.future.modules.crm.dto.StoreAllotImeDto
import net.myspring.future.modules.crm.dto.StoreAllotDto
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate


interface StoreAllotRepository : BaseRepository<StoreAllot, String> {

    @Query("""
    SELECT
        MAX(t1.business_id)
    FROM
        crm_store_allot t1
    WHERE
        t1.created_date >= ?1
        """, nativeQuery = true)
    fun findMaxBusinessId(localDate: LocalDate): String


    @Query("""
    SELECT
        t2.express_codes expressOrderExpressCodes,
        t2.express_company_id,
        t1.*
    FROM
        crm_store_allot t1
        LEFT JOIN crm_express_order t2 ON t1.express_order_id = t2.id
    WHERE
        t1.enabled = 1
        AND t1.id =  ?1
        """, nativeQuery = true)
    fun findStoreAllotDtoById(id: String): StoreAllotDto


}

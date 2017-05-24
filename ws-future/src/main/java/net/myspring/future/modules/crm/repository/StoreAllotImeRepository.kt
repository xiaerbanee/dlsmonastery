package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.domain.StoreAllot
import net.myspring.future.modules.crm.domain.StoreAllotIme
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


interface StoreAllotImeRepository : BaseRepository<StoreAllotIme, String> {

    fun findByProductImeId(productImeId: String): List<StoreAllotIme>

    @Query("""
    SELECT
        ime.ime productImeIme,
        ime.meid productImeMeid,
        product.name productName,
        t1.*
    FROM
        crm_store_allot_ime t1,
        crm_product product,
        crm_product_ime ime
    WHERE
        t1.store_allot_id = ?1
        AND t1.enabled = 1
        AND t1.product_id = product.id
        AND t1.product_ime_id = ime.id
        """, nativeQuery = true)
    fun findByStoreAllotId(storeAllotId: String): List<StoreAllotImeDto>

}

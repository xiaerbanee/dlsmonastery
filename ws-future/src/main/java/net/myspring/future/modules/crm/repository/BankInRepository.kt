package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.crm.domain.BankIn
import net.myspring.future.modules.crm.dto.BankInDto
import net.myspring.future.modules.crm.web.query.BankInQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime


interface BankInRepository : BaseRepository<BankIn, String> {

    fun findByShopIdAndType(shopId: String, type: String): BankIn

    @Query("""
    SELECT
        depot.name shopName,
        bank.name bankName,
        depot.client_id shopClientId,
        t1.*
    FROM
        crm_bank_in t1,
        crm_depot depot,
        crm_bank bank
    WHERE
        t1.enabled = 1
        AND t1.shop_id = depot.id
        AND depot.enabled = 1
        AND t1.bank_id = bank.id
        AND bank.enabled = 1
        AND t1.id = ?1
        """, nativeQuery = true)
    fun findDto(id: String): BankInDto

}

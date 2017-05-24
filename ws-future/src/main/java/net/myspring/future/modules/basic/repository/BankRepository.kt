package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

@CacheConfig(cacheNames = arrayOf("banks"))
interface BankRepository : BaseRepository<Bank, String> {
    @Cacheable
    override fun findOne(id: String): Bank

    @CachePut(key="#id")
    fun save(bank: Bank): Bank

    @Query("""
        SELECT t1.*
        FROM crm_bank t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Bank>

    fun findByName(name: String): Bank

    fun findByOutId(outId: String): Bank

    @Query("""
        DELETE FROM
        crm_account_bank
        where bank_id=?1
    """, nativeQuery = true)
    fun deleteBankAccount(bankId: String): Int

    fun saveAccount(bankId: String,accountIds: List<String>): Int

    @Query("""
        select
        t1.*
        from
        crm_bank t1,
        crm_account_bank t2
        where
        t1.enabled =1
        and t1.id=t2.bank_id
        and t2.account_id=?1
    """, nativeQuery = true)
    fun findByAccountId(accountId: String): List<Bank>

    @Query("""
        select
        MAX(out_date)
        from
        crm_bank t1
        where
        t1.enabled=1
    """, nativeQuery = true)
    fun getMaxOutDate(): LocalDateTime

    fun findByNameLike(name: String): List<Bank>

    @Query("""
        select account_id from crm_account_bank where bank_id=?1
    """, nativeQuery = true)
    fun findAccountIdList(id: String): List<String>
}

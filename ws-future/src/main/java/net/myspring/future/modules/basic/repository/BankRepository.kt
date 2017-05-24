package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

@CacheConfig(cacheNames = arrayOf("banks"))
interface BankRepository : BaseRepository<Bank, String> {
    @Cacheable
    override fun findOne(id: String): Bank

    @CachePut(key="#id")
    fun save(bank: Bank): Bank

    fun findAllEnabled(): List<Bank>

    fun findByName(name: String): Bank

    fun findByOutId(outId: String): Bank

    fun deleteBankAccount(@Param("bankId") bankId: String): Int

    fun saveAccount(@Param("bankId") bankId: String, @Param("accountIds") accountIds: List<String>): Int

    fun findByAccountId(accountId: String): List<Bank>

    val maxOutDate: LocalDateTime

    fun findByNameLike(name: String): List<Bank>

    fun findAccountIdList(id: String): List<String>
}

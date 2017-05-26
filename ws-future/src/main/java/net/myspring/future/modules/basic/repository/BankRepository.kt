package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.basic.dto.BankDto
import net.myspring.future.modules.basic.web.query.BankQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import javax.persistence.EntityManager

@CacheConfig(cacheNames = arrayOf("banks"))
interface BankRepository : BaseRepository<Bank, String>,BankRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Bank

    @CachePut(key="#id")
    fun save(bank: Bank): Bank

    @Query("""
        SELECT t1.*
        FROM crm_bank t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<Bank>

    fun findByName(name: String): Bank

    fun findByOutId(outId: String): Bank

    @Query("""
        DELETE FROM
        crm_account_bank
        where bank_id=?1
    """, nativeQuery = true)
    fun deleteBankAccount(bankId: String): Int

//    fun saveAccount(bankId: String,accountIds: MutableList<String>): Int

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
    fun findByAccountId(accountId: String): MutableList<Bank>

    @Query("""
        select
            MAX(out_date)
        from
            crm_bank t1
        where
            t1.enabled=1
    """, nativeQuery = true)
    fun getMaxOutDate(): LocalDateTime

    fun findByNameLike(name: String): MutableList<Bank>

    @Query("""
        select account_id from crm_account_bank where bank_id=?1
    """, nativeQuery = true)
    fun findAccountIdList(id: String): MutableList<String>
}

interface BankRepositoryCustom{
    fun findPage(pageable: Pageable, bankQuery: BankQuery): Page<BankDto>
}

class BankRepositoryImpl @Autowired constructor(val entityManager: EntityManager):BankRepositoryCustom{

    override fun findPage(pageable: Pageable, bankQuery: BankQuery): Page<BankDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*, GROUP_CONCAT(DISTINCT t2.account_id) AS 'accountIdStr'
            FROM
                crm_bank t1
            LEFT JOIN crm_account_bank t2 ON t2.bank_id = t1.id
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(bankQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        sb.append(""" GROUP by t1.name """)
        var query = entityManager.createNativeQuery(sb.toString(), BankDto::class.java)

        return query.resultList as Page<BankDto>
    }
}

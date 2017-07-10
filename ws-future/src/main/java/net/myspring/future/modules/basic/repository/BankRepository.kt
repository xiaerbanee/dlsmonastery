package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Bank
import net.myspring.future.modules.basic.dto.BankDto
import net.myspring.future.modules.basic.web.query.BankQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

@CacheConfig(cacheNames = arrayOf("banks"))
interface BankRepository : BaseRepository<Bank, String>,BankRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Bank

    @CachePut(key="#p0.id")
    fun save(bank: Bank): Bank

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
    """)
    fun findAllEnabled(): MutableList<Bank>

    fun findByName(name: String): Bank?

    fun findByEnabledIsTrueAndNameContaining(name: String): MutableList<Bank>

}

interface BankRepositoryCustom{

    fun findByAccountId(accountId: String): MutableList<Bank>

    fun deleteBankAccount(bankId: String): Int

    fun saveBankAccount(bankId: String,accountIdList: MutableList<String>):Int

    fun findAccountIdList(id: String): MutableList<String>

    fun findPage(pageable: Pageable, bankQuery: BankQuery): Page<BankDto>
}

class BankRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):BankRepositoryCustom{

    override fun findByAccountId(accountId: String): MutableList<Bank>{
        return namedParameterJdbcTemplate.query("""
        select
        t1.*
        from
        crm_bank t1,
        crm_account_bank t2
        where
        t1.enabled =1
        and t1.id=t2.bank_id
        and t2.account_id=:accountId
        """,Collections.singletonMap("accountId",accountId),BeanPropertyRowMapper(Bank::class.java))
    }

    override fun deleteBankAccount(bankId: String): Int{
        return namedParameterJdbcTemplate.update("""
          DELETE FROM
              crm_account_bank
          where bank_id=:bankId
        """,Collections.singletonMap("bankId",bankId))
    }

    override fun saveBankAccount(bankId: String,accountIdList: MutableList<String>):Int{

        val sb = StringBuilder("""
            INSERT INTO crm_account_bank(account_id,bank_id) values
        """)
        for(accountId in accountIdList){
            sb.append("("+accountId+","+bankId+"),")
        }
        sb.deleteCharAt(sb.length -1)
        return namedParameterJdbcTemplate.update(sb.toString(),HashMap<String,Any>())

    }

    override fun findAccountIdList(id: String): MutableList<String>{
        return namedParameterJdbcTemplate.queryForList("""
            SELECT
                t1.account_id
            FROM
                crm_account_bank t1
            WHERE
                t1.bank_id = :id
        """,Collections.singletonMap("id",id),String::class.java)
    }

    override fun findPage(pageable: Pageable, bankQuery: BankQuery): Page<BankDto> {
        val sb = StringBuilder("""
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

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(bankQuery), BeanPropertyRowMapper(BankDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(bankQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}

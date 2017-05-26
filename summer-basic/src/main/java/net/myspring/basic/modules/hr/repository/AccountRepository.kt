package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.config.MyBeanPropertyRowMapper
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Account
import net.myspring.basic.modules.hr.dto.AccountDto
import net.myspring.basic.modules.hr.web.query.AccountQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager


/**
 * Created by lihx on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("accounts"))
interface AccountRepository : BaseRepository<Account, String>,AccountRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Account

    @CachePut(key = "#id")
    fun save(account: Account): Int

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.loginName=?1
    """)
    fun findByLoginName(loginName: String): Account

    @Query("""
        SELECT t.password
        #{#entityName} t
        WHERE t.id=?1
    """)
    fun findAccountPassword(id: String): String

    @Query("""
        SELECT t1.id
        FROM  #{#entityName} t1
        where t1.enabled=1
        and t1.officeId IN ?1
    """)
    fun findByOfficeIds(officeIds: MutableList<String>): MutableList<Account>

    @Query("""
        SELECT
        t1.*
        FROM  #{#entityName} t1,Position t2
        where t1.positionId=t2.id
        and t2.id=?1
    """)
    fun findByPosition(positionId: String): MutableList<Account>

    @Query("""
        SELECT t1.*
        FROM  #{#entityName}
        WHERE t1.enabled=1
        and t1.loginName in ?1
    """)
    fun findByLoginNameList(loginNames: MutableList<String>): MutableList<Account>

    @Query("""
        SELECT t1.*
        FROM  #{#entityName} t1
        WHERE t1.id=?1
    """)
    fun findById(id: String): MutableList<Account>

    @Query("""
        SELECT t1.*
        FROM  #{#entityName} t1
        WHERE t1.id IN ?1
    """)
    fun findByIds(ids: MutableList<String>): MutableList<Account>
}

interface AccountRepositoryCustom{
    fun findByLoginNameLikeAndType(type: String, name: String): MutableList<Account>

    fun findPage(pageable: Pageable, accountQuery: AccountQuery): Page<AccountDto>

    fun findByFilter(accountQuery: AccountQuery): MutableList<Account>
}

class AccountRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AccountRepositoryCustom{
    override fun findByFilter(accountQuery: AccountQuery): MutableList<Account> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findPage(pageable: Pageable, accountQuery: AccountQuery): Page<AccountDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByLoginNameLikeAndType(type: String, name: String): MutableList<Account> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            hr_account t1
            where t1.enabled=1
            and t1.login_name LIKE %:name%)
        """)
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and t1.type=:type")
        }
        sb.append(" limit 0, 100")
        var paramMap = HashMap<String, Any>()
        paramMap.put("type", type)
        paramMap.put("name", name)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, MyBeanPropertyRowMapper(Account::class.java))


    }

}

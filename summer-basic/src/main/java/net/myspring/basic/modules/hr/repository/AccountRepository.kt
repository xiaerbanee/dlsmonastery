package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Account
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


/**
 * Created by lihx on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("accounts"))
interface AccountRepository : BaseRepository<Account, String> {
    @Cacheable
    override fun findOne(id: String): Account

    @CachePut(key = "#id")
    fun save(account: Account): Int

    @Query("""
        SELECT
        t1.*
        FROM
        hr_account t1
        where t1.enabled=1
        and t1.login_name=?1
    """, nativeQuery = true)
    fun findByLoginName(loginName: String): Account

    @Query("""
        SELECT t1.password
        FROM hr_account t1
        WHERE t1.id=?1
    """, nativeQuery = true)
    fun findAccountPassword(id: String): String

    @Query("""
        SELECT t1.id
        FROM hr_account t1
        where t1.enabled=1
        and t1.office_id IN ?1
    """, nativeQuery = true)
    fun findByOfficeIds(officeIds: List<String>): List<Account>

    @Query("""
        SELECT
        t1.*
        FROM
        hr_account t1,hr_position t2
        where t1.position_id=t2.id
        and t2.id=?1
    """, nativeQuery = true)
    fun findByPosition(positionId: String): List<Account>

    @Query("""
        SELECT t1.*
        FROM hr_account t1
        WHERE t1.enabled=1
        and t1.login_name in ?1
    """, nativeQuery = true)
    fun findByLoginNameList(loginNames: List<String>): List<Account>

    @Query("""
        SELECT t1.*
        FROM hr_account t1
        WHERE t1.id=?1
    """, nativeQuery = true)
    fun findById(id: String): List<Account>
}

interface AccountRepositoryCustom{
    fun findByLoginNameLikeAndType(type: String, name: String): List<Account>
}

class AccountRepositoryImpl @Autowired constructor(val entityManager: EntityManager): AccountRepositoryCustom{
    override fun findByLoginNameLikeAndType(type: String, name: String): List<Account> {
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
            sb.append("""
                and t1.type=:type
            """)
        }
        sb.append("""
            limit 0, 100
        """)
        var query = entityManager.createNativeQuery(sb.toString(), Account::class.java)
        query.setParameter("name", name)
        query.setParameter("type", type)
        return query.resultList as List<Account>

    }

}

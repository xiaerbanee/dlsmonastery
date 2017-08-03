package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Account
import net.myspring.basic.modules.hr.dto.AccountDto
import net.myspring.basic.modules.hr.web.query.AccountQuery
import net.myspring.cloud.modules.kingdee.domain.StkInventory
import net.myspring.util.collection.CollectionUtil
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
import java.time.LocalDate
import java.util.*


/**
 * Created by lihx on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("accounts"))
interface AccountRepository : BaseRepository<Account, String>,AccountRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Account


    @CachePut(key = "#p0.id")
    fun save(account: Account): Account

    fun findByEmployeeIdAndType(employeeId:String,type:String):Account

    fun findByEmployeeIdInAndType(employeeIdList :List<String>,type:String): MutableList<Account>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.loginName=?1
    """)
    fun findByLoginName(loginName: String): Account

    @Query("""
        SELECT t.password
        FROM #{#entityName} t
        WHERE t.id=?1
    """)
    fun findAccountPassword(id: String): String

    fun findByOfficeIdIn(officeIds: MutableList<String>): MutableList<Account>

    @Query("""
        SELECT
        t1
        FROM  #{#entityName} t1,Position t2
        where t1.positionId=t2.id
        and t2.id=?1
    """)
    fun findByPosition(positionId: String): MutableList<Account>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE t.enabled=1
        and t.loginName in ?1
    """)
    fun findByLoginNameList(loginNames: MutableList<String>): MutableList<Account>
}

interface AccountRepositoryCustom{
    fun findByLoginNameLikeAndType(type: String, loginName: String): MutableList<AccountDto>

    fun findPage(pageable: Pageable, accountQuery: AccountQuery): Page<AccountDto>

    fun findByFilter(accountQuery: AccountQuery): MutableList<Account>

    fun findByAccessLoginNameList(loginNameList:MutableList<String>,date:LocalDate): MutableList<Account>

    fun findDto(id:String):AccountDto

    fun findDtoByIdList(idList:MutableList<String>):MutableList<AccountDto>
}

class AccountRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AccountRepositoryCustom{
    override fun findDtoByIdList(idList: MutableList<String>): MutableList<AccountDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
	        t2.name AS employeeName,
	        t3.name AS positionName,
	        office.name AS officeName,
	        leader.login_name AS leaderName,
	        t2.entry_date,
	        t2.leave_date,
	        t2.regular_date,
	        t2.status AS employeeStatus,
	        t1.*
        FROM
	        hr_account t1
	        LEFT JOIN hr_employee t2 ON t1.employee_id = t2.id
	        LEFT JOIN hr_position t3 ON t1.position_id = t3.id
	        LEFT JOIN sys_office office ON t1.office_id = office.id
	        LEFT JOIN hr_account leader ON t1.leader_id = leader.id
        WHERE
	        t1.enabled = 1
            and t1.id in (:idList)
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("idList", idList)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AccountDto::class.java))
    }

    override fun findDto(id: String): AccountDto {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*,t2.name as employeeName,t3.name as positionName,office.name as officeName,leader.login_name as leaderName,
            t2.entry_date,t2.leave_date,t2.regular_date,t2.status as employeeStatus
            FROM
             hr_account t1 left join hr_employee t2 on t1.employee_id=t2.id
            left join hr_position t3 on t1.position_id=t3.id
            left join sys_office office on t1.office_id=office.id
            left join hr_account leader on t1.leader_id=leader.id
            WHERE
            t1.enabled=1
            and t1.id=:id
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("id", id)
        return namedParameterJdbcTemplate.queryForObject(sb.toString(), paramMap, BeanPropertyRowMapper(AccountDto::class.java))
    }

    override fun findByAccessLoginNameList(loginNameList: MutableList<String>, date: LocalDate): MutableList<Account> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            hr_account t1,hr_employee t2
            WHERE
            t1.enabled=1
            and t1.employee_id=t2.id
            and t1.login_name in(:loginNameList)
            and (t2.leave_date is null or t2.leave_date > :date)
        """)
        var paramMap = HashMap<String, Any>()
        paramMap.put("loginNameList", loginNameList)
        paramMap.put("date", date)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(Account::class.java))
    }

    override fun findByFilter(accountQuery: AccountQuery): MutableList<Account> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*
            FROM
            hr_account t1
            WHERE
            t1.enabled=1
        """)
        if (accountQuery.loginName != null) {
            sb.append(" AND t1.login_name LIKE CONCAT('%',:loginName,'%') ")
        }
        if(CollectionUtil.isNotEmpty(accountQuery.officeIdList)) {
            sb.append(" and t1.office_id  IN (:officeIdList)");
        }
        if (accountQuery.officeId != null) {
            sb.append(" AND t1.office_id = :officeId ")
        }
        if (accountQuery.officeName != null) {
            sb.append("""
                and t1.office_id in(
                select t2.id
                from sys_office t2
                where t2.name = :officeName
            )
            """)
        }
        if (accountQuery.positionId != null) {
            sb.append(" and t1.position_id =:positionId ")
        }
        if (accountQuery.employeeName != null) {
            sb.append("""
                and t1.employee_id in(
                select t4.id
                from hr_employee t4
                where t4.name LIKE CONCAT('%',:employeeName,'%')
                )
            """)
        }

        if (accountQuery.leaderName != null) {
            sb.append("""
                AND t1.leader_id in (
                select t1.id
                from hr_account t1
                where t1.login_name LIKE CONCAT('%',:leaderName,'%')
                )
            """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(accountQuery),BeanPropertyRowMapper(Account::class.java))
    }

    override fun findPage(pageable: Pageable, accountQuery: AccountQuery): Page<AccountDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*,t2.name as employeeName,t3.name as positionName,office.name as officeName,leader.login_name as leaderName,
            t2.entry_date,t2.leave_date,t2.regular_date,t2.status as employeeStatus
            FROM
            hr_account t1 left join hr_employee t2 on t1.employee_id=t2.id
            left join hr_position t3 on t1.position_id=t3.id
            left join sys_office office on t1.office_id=office.id
            left join hr_account leader on t1.leader_id=leader.id
            WHERE
            t1.enabled=1
        """)
        if (accountQuery.loginName != null) {
            sb.append(" AND t1.login_name LIKE CONCAT('%',:loginName,'%') ")
        }
        if (accountQuery.officeId != null) {
            sb.append(" AND t1.office_id = :officeId ")
        }
        if (CollectionUtil.isNotEmpty(accountQuery.officeIdList)) {
            sb.append(" AND t1.office_id in ( :officeIdList) ")
        }
        if (accountQuery.positionId != null) {
            sb.append("""
                and t1.position_id =:positionId
            """)
        }
        if (accountQuery.employeeName != null) {
            sb.append("""
                and t2.name LIKE CONCAT('%',:employeeName,'%')
            """)
        }

        if (accountQuery.leaderName != null) {
            sb.append("""
                AND leader.login_name LIKE CONCAT('%',:leaderName,'%')
            """)
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql,BeanPropertySqlParameterSource(accountQuery),BeanPropertyRowMapper(AccountDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(accountQuery),Long::class.java);
        return PageImpl(list,pageable,count);

    }

    override fun findByLoginNameLikeAndType(type: String, loginName: String): MutableList<AccountDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*,t2.status as employeeStatus,position.name as positionName
            FROM
            hr_account t1,hr_employee t2,hr_position position
            where t1.enabled=1
            and t1.employee_id=t2.id
            and t1.position_id=position.id
            and t1.login_name LIKE CONCAT('%',:name,'%')
        """)
        if (StringUtils.isNotBlank(type)) {
            sb.append(" and t1.type=:type")
        }
        sb.append(" limit 0, 100")
        var paramMap = HashMap<String, Any>()
        paramMap.put("type", type)
        paramMap.put("name", loginName)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, BeanPropertyRowMapper(AccountDto::class.java))
    }

}

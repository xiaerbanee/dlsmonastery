package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Account
import net.myspring.basic.modules.hr.domain.AccountWeixin
import net.myspring.basic.modules.hr.dto.AccountDto
import net.myspring.basic.modules.hr.dto.AccountWeixinDto
import net.myspring.basic.modules.hr.web.query.AccountQuery
import net.myspring.basic.modules.hr.web.query.AccountWeixinQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate

/**
 * Created by lihx on 2017/5/24.
 */
interface AccountWeixinRepository : BaseRepository<AccountWeixin,String>,AccountWeixinRepositoryCustom{

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.accountId=?1
    """)
    fun findByAccountId(accountId: String): AccountWeixin
}

interface AccountWeixinRepositoryCustom{

    fun findPage(pageable: Pageable, accountWeixinQuery: AccountWeixinQuery): Page<AccountWeixinDto>
}


class AccountWeixinRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AccountWeixinRepositoryCustom{
    override fun findPage(pageable: Pageable, accountWeixinQuery: AccountWeixinQuery): Page<AccountWeixinDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
            t1.*,t3.login_name as accountName,office.name as officeName,area.name as 'areaName'
            FROM
            hr_account_weixin t1,sys_office office,hr_account t3,sys_office area
            WHERE
            t1.enabled=1
            and t3.office_id=office.id
            and t1.account_id=t3.id
            and office.area_id=area.id
        """)
        if (accountWeixinQuery.accountName != null) {
            sb.append("""
                and t3.login_name LIKE CONCAT ('%',:accountName,'%')
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(accountWeixinQuery), BeanPropertyRowMapper(AccountWeixinDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(accountWeixinQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}

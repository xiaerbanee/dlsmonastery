package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook
import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by haos on 2017/5/24.
 */
interface  AccountKingdeeBookRepository : BaseRepository<AccountKingdeeBook,String>,AccountKingdeeBookRepositoryCustom{
    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled = 1
        and t.accountId = :accountId
     """)
    fun findByAccountId (@Param("accountId")accountId:String): AccountKingdeeBook

}

interface AccountKingdeeBookRepositoryCustom{
    fun findPage(pageable: Pageable, accountKingdeeBookQuery: AccountKingdeeBookQuery): Page<AccountKingdeeBookDto>?
}

class AccountKingdeeBookRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AccountKingdeeBookRepositoryCustom{
    override fun findPage(pageable: Pageable, accountKingdeeBookQuery: AccountKingdeeBookQuery): Page<AccountKingdeeBookDto>? {
        var sb = StringBuilder("""
                select t1.id,t1.username,t1.account_id,t1.company_id,t2.name as kingdeeBookName,t2.type as kingdeeBookType
                from sys_account_kingdee_book t1, sys_kingdee_book t2
                where t1.kingdee_book_id = t2.id and t1.enabled=1
            """)
        if(StringUtils.isNoneBlank(accountKingdeeBookQuery.accountId)){
            sb.append(" and t1.account_id = :accountId ")
        }
        if (StringUtils.isNoneBlank(accountKingdeeBookQuery.companyId)){
            sb.append(" and t1.company_id = :companyId ")
        }
        if (StringUtils.isNoneBlank(accountKingdeeBookQuery.username)){
            sb.append(" and t1.username LIKE CONCAT('%',:username,'%') ")
        }
        if (StringUtils.isNoneBlank(accountKingdeeBookQuery.kingdeeBookName)){
            sb.append(" and t2.name = :kingdeeBookName ")
        }
        if (StringUtils.isNoneBlank(accountKingdeeBookQuery.kingdeeBookType)){
            sb.append(" and t2.type = :kingdeeBookType ")
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(accountKingdeeBookQuery), BeanPropertyRowMapper(AccountKingdeeBookDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(accountKingdeeBookQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}
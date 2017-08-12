package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountFavorite
import net.myspring.basic.modules.hr.dto.AccountFavoriteDto
import net.myspring.basic.modules.hr.web.form.AccountFavoriteForm
import net.myspring.basic.modules.hr.web.query.AccountFavoriteQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

interface AccountFavoriteRepository : BaseRepository<AccountFavorite, String>,AccountFavoriteRepositoryCustom {

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.accountId=?1
        and t.parentId=null
    """)
    fun findByAccountIdAndParentIdIsNull(accountId:String):MutableList<AccountFavorite>

    @Query("""
             SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.accountId=?1
        and t.parentId is not null
    """)
    fun findByAccountIdAndParentIdIsNotNull(accountId:String):MutableList<AccountFavorite>
}

interface AccountFavoriteRepositoryCustom {

    fun findPage(pageable:Pageable,ccountFavoriteQuery:AccountFavoriteQuery): Page<AccountFavoriteDto>
}


class AccountFavoriteRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AccountFavoriteRepositoryCustom {

    override fun findPage(pageable: Pageable, accountFavoriteQuery: AccountFavoriteQuery): Page<AccountFavoriteDto> {
        var sb = StringBuilder()
        sb.append("""
            select
            t1.*,t2.name as parentName
            from
            hr_account_favorite t1 left join hr_account_favorite t2 on t1.parent_id=t2.id
            where
            t1.enabled=1

        """)
        if (StringUtils.isNotEmpty(accountFavoriteQuery.accountId)) {
            sb.append("""
            and t1.account_id=:accountId
            and t2.account_id=:accountId            """)
        }
        if (StringUtils.isNotEmpty(accountFavoriteQuery.name)) {
            sb.append("""
                and t1.name LIKE CONCAT ('%',:name,'%')
            """)
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(), pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(accountFavoriteQuery), BeanPropertyRowMapper(AccountFavoriteDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(accountFavoriteQuery), Long::class.java)
        return PageImpl(list, pageable, count)
    }
}
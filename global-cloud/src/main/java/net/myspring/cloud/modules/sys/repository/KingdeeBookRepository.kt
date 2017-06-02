package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.KingdeeBook
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery
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
interface  KingdeeBookRepository : BaseRepository<KingdeeBook,String>,KingdeeBookRepositoryCustom{
    @Query("""
        select t1
        FROM  #{#entityName} t1,AccountKingdeeBook t2
        where t1.enabled = 1
        and t2.enabled=1
        and t1.id = t2.kingdeeBookId
        and t2.accountId = :accountId
     """)
    fun findByAccountId(@Param("accountId")accountId:String): KingdeeBook

    @Query("""
        SELECT t1.name
         FROM  #{#entityName} t1
        where t1.enabled = 1
     """)
    fun findNames():MutableList<String>

    @Query("""
        SELECT distinct t1.type
        FROM  #{#entityName} t1
        where t1.enabled = 1
     """)
    fun findTypes():MutableList<String>
}

interface KingdeeBookRepositoryCustom{
    fun findPage(pageable: Pageable, kingdeeBookQuery: KingdeeBookQuery): Page<KingdeeBook>?
}

class KingdeeBookRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): KingdeeBookRepositoryCustom{
    override fun findPage(pageable: Pageable, kingdeeBookQuery: KingdeeBookQuery): Page<KingdeeBook>? {
        var sb = StringBuilder("select * from sys_kingdee_book where enabled=1 ");
        if(StringUtils.isNotBlank(kingdeeBookQuery.companyId)) {
            sb.append(" and company_id = :companyId ");
        }
        if(StringUtils.isNotBlank(kingdeeBookQuery.name)){
            sb.append(" and name = :name ");
        }
        if(StringUtils.isNotBlank(kingdeeBookQuery.type)){
            sb.append(" and type = :type ");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(kingdeeBookQuery), BeanPropertyRowMapper(KingdeeBook::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(kingdeeBookQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}
package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.KingdeeSyn
import net.myspring.cloud.modules.sys.web.query.KingdeeSynQuery
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
 * Created by lihx on 2017/6/21.
 */
interface  KingdeeSynRepository : BaseRepository<KingdeeSyn, String>,KingdeeSynRepositoryCustom{
    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled = 1
        and t.extendId = :extendId
        and t.extendType = :extendType
     """)
    fun findByExtendIdAndExtendType (@Param("extendId")extendId:String,@Param("extendType")extendType:String): MutableList<KingdeeSyn>?
}

interface KingdeeSynRepositoryCustom{
    fun findPage(pageable: Pageable, kingdeeBookQuery: KingdeeSynQuery): Page<KingdeeSyn>?
}

class KingdeeSynRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): KingdeeSynRepositoryCustom{
    override fun findPage(pageable: Pageable, kingdeeSynQuery: KingdeeSynQuery): Page<KingdeeSyn>? {
        var sb = StringBuilder("select * from sys_kingdee_syn where enabled=1 ");
        if(kingdeeSynQuery.createdDate != null){
            sb.append(" and date(created_date) = :createdDate ");
        }
        if(StringUtils.isNotBlank(kingdeeSynQuery.billNo)){
            sb.append(" and bill_no like CONCAT('%',:billNo,'%')");
        }
        if(StringUtils.isNotBlank(kingdeeSynQuery.extendId)){
            sb.append(" and extend_id like CONCAT('%',:extendId,'%') ");
        }
        if(StringUtils.isNotBlank(kingdeeSynQuery.extendType)){
            sb.append(" and extend_type = :extendType ");
        }
        if(kingdeeSynQuery.success != null){
            sb.append(" and success = :success ");
        }
        if(kingdeeSynQuery.locked != null){
            sb.append(" and locked = :locked ");
        }
        if(StringUtils.isNotBlank(kingdeeSynQuery.kingdeeBookId)){
            sb.append(" and kingdee_book_id = :kingdeeBookId ");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(kingdeeSynQuery), BeanPropertyRowMapper(KingdeeSyn::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(kingdeeSynQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}
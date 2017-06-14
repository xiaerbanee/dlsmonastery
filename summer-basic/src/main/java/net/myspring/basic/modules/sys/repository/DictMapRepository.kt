
package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.DictMap
import net.myspring.basic.modules.sys.dto.DictEnumDto
import net.myspring.basic.modules.sys.dto.DictMapDto
import net.myspring.basic.modules.sys.web.query.DictMapQuery
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
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("dictMaps"))
interface  DictMapRepository :BaseRepository<DictMap,String>, DictMapRepositoryCustom{
    @CachePut(key="#p0.id")
    fun save(dictMap: DictMap): DictMap

    @Cacheable
    override fun findOne(id: String): DictMap

    @Query("""
        SELECT DISTINCT
        t.category
        FROM  #{#entityName} t
     """)
    fun findDistinctCategory():MutableList<String>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.category=?1
        and t.enabled=1
     """)
    fun findByCategory(category:String):MutableList<DictMap>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.name=?1
        and t.enabled=1
     """)
    fun findByName(name:String): DictMap


}


interface DictMapRepositoryCustom{
    fun findPage(pageable: Pageable, dictMapQuery: DictMapQuery): Page<DictMapDto>?


}

class DictMapRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DictMapRepositoryCustom{
    override fun findPage(pageable: Pageable, dictMapQuery: DictMapQuery): Page<DictMapDto>? {
        var sb = StringBuilder("select * from sys_dict_map where enabled=1 ");
        if(StringUtils.isNotBlank(dictMapQuery.category)) {
            sb.append(" and category like concat('%',:category,'%')");
        }
        if(dictMapQuery.createdDateStart != null) {
            sb.append(" and created_date > :createdDateStart ");
        }
        if(dictMapQuery.createdDateEnd != null) {
            sb.append(" and created_date < :createdDateEnd ");
        }
        if(dictMapQuery.value != null) {
            sb.append(" and value = :value ");
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(dictMapQuery), BeanPropertyRowMapper(DictMapDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(dictMapQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }


}
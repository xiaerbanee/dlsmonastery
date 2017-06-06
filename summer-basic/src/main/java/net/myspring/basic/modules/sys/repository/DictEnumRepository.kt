package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.DictEnum
import net.myspring.basic.modules.sys.dto.DictEnumDto
import net.myspring.basic.modules.sys.web.query.DictEnumQuery
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
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("dictEnums"))
interface DictEnumRepository :BaseRepository<DictEnum,String>,DictEnumRepositoryCustom{
    @CachePut(key="#p0.id")
    fun save(dictEnum: DictEnum): DictEnum

    @Cacheable
    override  fun findOne(id: String): DictEnum

    fun findByCategoryIn(categoryList: MutableList<String>): MutableList<DictEnum>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.enabled=1
        and t.category=:category order by t.sort asc
     """)
    fun findByCategory(@Param("category")category:String):MutableList<DictEnum>

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
        t.enabled=1
        and t.value=:value
     """)
    fun findByValue(@Param("value")value:String): DictEnum


}


interface DictEnumRepositoryCustom{
    fun findPage(pageable: Pageable, dictEnumQuery: DictEnumQuery): Page<DictEnumDto>?


}

class DictEnumRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DictEnumRepositoryCustom{
    override fun findPage(pageable: Pageable, dictEnumQuery: DictEnumQuery): Page<DictEnumDto>? {
       var sb = StringBuilder("select * from sys_dict_enum where enabled=1 ");
        if(StringUtils.isNotBlank(dictEnumQuery.category)) {
            sb.append(" and category like concat('%',:category,'%')");
        }
        if(dictEnumQuery.createdDateStart != null) {
            sb.append(" and created_date > :createdDateStart ");
        }
        if(dictEnumQuery.createdDateEnd != null) {
            sb.append(" and created_date < :createdDateEnd ");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql,BeanPropertySqlParameterSource(dictEnumQuery),BeanPropertyRowMapper(DictEnumDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(dictEnumQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }


}
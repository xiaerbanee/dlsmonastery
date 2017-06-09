package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.BackendModule
import net.myspring.basic.modules.sys.dto.BackendModuleDto
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery
import net.myspring.util.repository.MySQLDialect
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
@CacheConfig(cacheNames = arrayOf("backendModules"))
interface  BackendModuleRepository:BaseRepository<BackendModule,String>,BackendModuleRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): BackendModule

    @CachePut(key="#p0.id")
    fun save(backendModule: BackendModule): BackendModule

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.backendId IN ?1
     """)
    fun findByBackendIds( backendIds:MutableList<String>):MutableList<BackendModule>

    @Query("""
         SELECT t1
         FROM  #{#entityName} t1,RoleModule t2
          where t1.enabled=1
          and t2.enabled=1
          and t2.backendModuleId=t1.id
          and t2.roleId=:roleId
     """)
    fun findByRoleId(@Param("roleId")roleId:String):MutableList<BackendModule>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE t.enabled=1
     """)
    fun findAllEnabled():MutableList<BackendModule>
}


interface BackendModuleRepositoryCustom{
    fun findPage(pageable: Pageable,backendModuleQuery: BackendModuleQuery): Page<BackendModuleDto>?
}

class BackendModuleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BackendModuleRepositoryCustom{
    override fun findPage(pageable: Pageable, backendModuleQuery: BackendModuleQuery): Page<BackendModuleDto>? {
        var sql = StringBuilder("""
                SELECT
                t1.*
                FROM
                sys_backend_module t1
                WHERE
                t1.enabled=1
            """);
        if(backendModuleQuery.name!=null){
            sql.append("""
                    AND t1.name LIKE CONCAT('%',:name,'%')
                """);
        }
        if(backendModuleQuery.backendName!=null){
            sql.append("""
                    and t1.backend_id in (
                    select id
                    from sys_backend
                    where name like CONCAT('%',:backendName,'%')
                )
                """);
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sql.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sql.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(backendModuleQuery), BeanPropertyRowMapper(BackendModuleDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(backendModuleQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }



}
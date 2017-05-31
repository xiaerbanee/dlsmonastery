package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Permission
import net.myspring.basic.modules.sys.dto.OfficeRuleDto
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.web.query.PermissionQuery
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
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("permissions"))
interface  PermissionRepository: BaseRepository<Permission, String>,PermissionRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Permission

    @CachePut(key="#id")
    fun save(permission: Permission): Permission

    @Query("""
        SELECT t1
        FROM
            #{#entityName} t1,
            RolePermission t2
        WHERE
            t1.id = t2.permissionId
        AND t2.roleId = ?1
        AND t1.enabled = 1
        AND t2.enabled = 1
     """)
    fun findByRoleId(roleId: String): MutableList<Permission>

    @Query("""
        SELECT t1
        FROM
            #{#entityName} t1,
            RolePermission t2,
            AccountPermission t3
        WHERE
            t1.id = t2.permissionId
        and t2.permissionId=t3.permissionId
        AND t2.roleId = ?1
        and t3.accountId=?2
        AND t1.enabled = 1
        AND t3.enabled = 1
        AND t2.enabled = 1
     """)
    fun findByRoleAndAccount(roleId: String, accountId: String): MutableList<Permission>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.menuId = ?1
     """)
    fun findByMenuId(menuId: String): MutableList<Permission>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.enabled=1
        and t.menuId IN ?1
     """)
    fun findByMenuIds(menuIds: MutableList<String>): MutableList<Permission>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.permission = ?1
     """)
    fun findByPermission(permissionStr: String): Permission

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.permission LIKE %?1%
     """)
    fun findByPermissionLike(permissionStr: String): MutableList<Permission>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
    """)
    fun findAllEnabled(): MutableList<Permission>



}



interface PermissionRepositoryCustom{


    fun logicDeleteByIds(removePermissionIds:MutableList<String>)

    fun findPage(pageable: Pageable, permissionQuery: PermissionQuery): Page<PermissionDto>?
}

class PermissionRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):PermissionRepositoryCustom{
    override fun logicDeleteByIds(removePermissionIds: MutableList<String>) {

    }

    override fun findPage(pageable: Pageable, permissionQuery: PermissionQuery): Page<PermissionDto>? {
        var sb = StringBuilder("select t1.* from sys_permission t1 where t1.enabled=1 ");
        if(StringUtils.isNotBlank(permissionQuery.name)) {
            sb.append(" and t1.name like concat('%',:name,'%')");
        }
        if(StringUtils.isNotBlank(permissionQuery.permission)) {
            sb.append(" and  t1.permission like concat('%',:permission,'%')");
        }
        if(StringUtils.isNotBlank(permissionQuery.menuName)) {
            sb.append(" and t1.menu_id in ( select t2.id from sys_menu t2 where t2.name LIKE CONCAT('%',:menuName,'%'))");
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(permissionQuery), BeanPropertyRowMapper(PermissionDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(permissionQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}
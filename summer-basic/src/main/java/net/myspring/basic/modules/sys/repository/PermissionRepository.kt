package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Permission
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.web.query.PermissionQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

class PermissionRepositoryImpl @Autowired constructor(val entityManager: EntityManager):PermissionRepositoryCustom{
    override fun logicDeleteByIds(removePermissionIds: MutableList<String>) {

    }

    override fun findPage(pageable: Pageable, permissionQuery: PermissionQuery): Page<PermissionDto>? {
        return null
    }

}
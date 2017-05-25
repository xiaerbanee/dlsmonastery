package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Permission
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.web.query.PermissionQuery
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  PermissionRepository: BaseRepository<Permission, String> {

    @Cacheable
    override fun findOne(id: String): Permission

    @CachePut(key="#id")
    fun save(permission: Permission): Int

    @Query("""
      SELECT
            t1.*
        FROM
            sys_permission t1,
            sys_role_permission t2
        WHERE
            t1.id = t2.permission_id
        AND t2.role_id = ?1
        AND t1.enabled = 1
        AND t2.enabled = 1
     """, nativeQuery = true)
    fun findByRoleId(roleId: String): List<Permission>

    @Query("""
             SELECT
            t1.*
        FROM
            sys_permission t1,
            sys_role_permission t2,
            hr_account_permission t3
        WHERE
            t1.id = t2.permission_id
        and t2.permission_id=t3.permission_id
        AND t2.role_id = ?1
        and t3.account_id=?2
        AND t1.enabled = 1
        AND t3.enabled = 1
        AND t2.enabled = 1
     """, nativeQuery = true)
    fun findByRoleAndAccount(roleId: String, accountId: String): List<Permission>

    @Query("""
             SELECT
        t1.*
        FROM
        sys_permission t1
        where
        t1.menu_id = ?1
     """, nativeQuery = true)
    fun findByMenuId(menuId: String): List<Permission>

    @Query("""
            SELECT
        t1.*
        FROM
        sys_permission t1
        where
        t1.enabled=1
        and t1.menu_id IN ?1
     """, nativeQuery = true)
    fun findByMenuIds(menuIds: List<String>): List<Permission>

    @Query("""
        SELECT
        t1.*
        FROM
        sys_permission t1
        where
        t1.permission = ?1
     """, nativeQuery = true)
    fun findByPermission(permissionStr: String): Permission

    @Query("""
         SELECT
        t1.*
        FROM
        sys_permission t1
        where
        t1.permission LIKE %?1%
     """, nativeQuery = true)
    fun findByPermissionLike(permissionStr: String): List<Permission>

    @Query("""
        SELECT t1.*
        FROM sys_permission t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Permission>

    fun logicDeleteByIds(removePermissionIds:List<String>)

    fun findPage(pageable: Pageable, permissionQuery: PermissionQuery): Page<PermissionDto>

}
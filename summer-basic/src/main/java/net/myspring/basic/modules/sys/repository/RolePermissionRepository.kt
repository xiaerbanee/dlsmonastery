package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.RolePermission
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query


/**
 * Created by haos on 2017/5/25.
 */
interface RolePermissionRepository: BaseRepository<RolePermission, String> {
    @Cacheable
    override fun findOne(id: String): RolePermission

    @CachePut(key="#id")
    fun save(RolePermission: RolePermission): Int

    @Query("""
        SELECT t1.*
        FROM  sys_role_permission t1
        where t1.enabled=1
        and t1.role_id=?1
     """, nativeQuery = true)
    fun findByRoleId(roleId: String): List<RolePermission>

    @Query("""
        SELECT t1.*
        FROM  sys_role_permission t1
        where  t1.role_id=?1
     """, nativeQuery = true)
    fun findAllByRoleId(roleId: String): List<RolePermission>

    @Query("""
     UPDATE  sys_role_permission
     SET enabled=?1
     where role_id=?2
     """, nativeQuery = true)
    fun setEnabledByRoleId(enabled: Boolean, roleId: String): Int

    @Query("""
         UPDATE  sys_role_permission
         SET enabled=?1
         where permission_id in ?2
     """, nativeQuery = true)
    fun setEnabledByPermissionIdList(enabled: Boolean,  permissionIdList: List<String>): Int

    @Query("""
        SELECT t1.*
        FROM  sys_role_permission t1
        where  t1.permission_id=?1
     """, nativeQuery = true)
    fun findAllByPermissionId(permissionId: String): List<RolePermission>

    @Query("""
       UPDATE  sys_role_permission
       SET enabled=?1
       where role_id=?2
     """, nativeQuery = true)
    fun setEnabledByPermissionId(enabled: Boolean, permissionId: String): Int

    @Query("""
        UPDATE  sys_role_permission
        SET enabled=?1
        where role_id in ?2
     """, nativeQuery = true)
    fun setEnabledByRoleIdList( enabled: Boolean,  roleIdList: List<String>): Int
}

package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.RoleModule
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query


/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("roleModules"))
interface RoleModuleRepository: BaseRepository<RoleModule, String> {

    @Cacheable
    override fun findOne(id: String): RoleModule

    @CachePut(key="#id")
    fun save(roleModule: RoleModule): Int

    @Query("""
            SELECT t1.*
        FROM  sys_role_module t1
        where t1.enabled=1
        and t1.role_id=?1
     """, nativeQuery = true)
    fun findByRoleId(roleId: String): MutableList<RoleModule>

    @Query("""
             SELECT t1.*
        FROM  sys_role_module t1
        where  t1.role_id=?1
     """, nativeQuery = true)
    fun findAllByRoleId(roleId: String): MutableList<RoleModule>

    @Query("""
              UPDATE  sys_role_module
               SET enabled=?1
               where role_id=?2
     """, nativeQuery = true)
    fun setEnabledByRoleId(enabled: Boolean, roleId: String): Int

    @Query("""
            UPDATE  sys_role_module
            SET enabled=?1
            where backend_module_id in ?2
     """, nativeQuery = true)
    fun setEnabledByModuleIdList(enabled: Boolean, moduleIds: MutableList<String>): Int

    @Query("""
            UPDATE  sys_role_module
            SET enabled=?1
            where backend_module_id in ?2
     """, nativeQuery = true)
    //TODO 修改sql
    fun batchSave(addRoleModules:MutableList<RoleModule>)
}
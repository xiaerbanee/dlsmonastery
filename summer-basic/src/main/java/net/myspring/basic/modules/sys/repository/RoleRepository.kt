package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Role
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.dto.RoleDto
import net.myspring.basic.modules.sys.web.query.PermissionQuery
import net.myspring.basic.modules.sys.web.query.RoleQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("roles"))
interface RoleRepository: BaseRepository<Role, String>,RoleRepositoryCustom {
    @Cacheable
    override fun findOne(id: String): Role

    @CachePut(key="#id")
    fun save(role: Role): Int

    @Query("""
          SELECT t1.*
        FROM sys_role t1
        where t1.enabled=1
        and t1.name like %?1%
     """, nativeQuery = true)
    fun findByNameLike(name: String): MutableList<Role>

    @Query("""
       SELECT t1.*
        from sys_role t1,hr_position t2,hr_account t3
        where t1.enabled=1
        and t3.position_id=t2.id
        and t2.role_id=t1.id
        and t3.enabled=1
        and t2.enabled=1
        and t3.id=?1
     """, nativeQuery = true)
    fun findByAccountId(accountId: String): Role


}



interface RoleRepositoryCustom{

    fun findPage(pageable: Pageable, roleQuery: RoleQuery): Page<RoleDto>?
}

class RoleRepositoryImpl @Autowired constructor(val entityManager: EntityManager):RoleRepositoryCustom{
    override fun findPage(pageable: Pageable, roleQuery: RoleQuery): Page<RoleDto>? {
        return null
    }


}
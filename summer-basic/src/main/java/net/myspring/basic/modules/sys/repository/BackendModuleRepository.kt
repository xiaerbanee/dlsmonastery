package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Backend
import net.myspring.basic.modules.sys.domain.BackendModule
import net.myspring.basic.modules.sys.dto.BackendDto
import net.myspring.basic.modules.sys.dto.BackendModuleDto
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery
import net.myspring.basic.modules.sys.web.query.BackendQuery
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
@CacheConfig(cacheNames = arrayOf("backendModules"))
interface  BackendModuleRepository:BaseRepository<BackendModule,String>,BackendModuleRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): BackendModule

    @CachePut(key="#id")
    fun save(backendModule: BackendModule): BackendModule

    @Query("""
         SELECT t1.*
        FROM  sys_backend_module  t1
        where t1.enabled=1
        and t1.backend_id IN ?1
     """, nativeQuery = true)
    fun findByBackendIds( backendIds:List<String>):List<BackendModule>

    @Query("""
         SELECT t1.*
         FROM  sys_backend_module  t1,sys_role_module t2
          where t1.enabled=1
          and t2.enabled=1
          and t2.backend_module_id=t1.id
          and t2.role_id=:roleId
     """, nativeQuery = true)
    fun findByRoleId(@Param("roleId")roleId:String):List<BackendModule>


}


interface BackendModuleRepositoryCustom{

    fun findPage(pageable: Pageable,backendModuleQuery: BackendModuleQuery): Page<BackendModuleDto>?

    fun findAllEnabled():List<BackendModule>?

}

class BackendModuleRepositoryImpl @Autowired constructor(val entityManager: EntityManager): BackendModuleRepositoryCustom{
    override fun findPage(pageable: Pageable, backendModuleQuery: BackendModuleQuery): Page<BackendModuleDto>? {
        return null
    }

    override fun findAllEnabled(): List<BackendModule>? {
       return null
    }


}
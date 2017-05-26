package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.BackendModule
import net.myspring.basic.modules.sys.dto.BackendModuleDto
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery
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

class BackendModuleRepositoryImpl @Autowired constructor(val entityManager: EntityManager): BackendModuleRepositoryCustom{
    override fun findPage(pageable: Pageable, backendModuleQuery: BackendModuleQuery): Page<BackendModuleDto>? {
        return null
    }



}
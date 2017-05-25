package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DepotStore
import net.myspring.future.modules.basic.dto.DepotStoreDto
import net.myspring.future.modules.basic.web.query.DepotStoreQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("depotStores"))
interface DepotStoreRepository : BaseRepository<DepotStore,String>,DepotStoreRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): DepotStore

    override fun findAll(): List<DepotStore>

    @CachePut(key = "#id")
    fun save(depotStore: DepotStore): Int

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<DepotStore>

    @Query("""
        SELECT t1.*
        FROM crm_depot_store t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<DepotStore>

}

interface DepotStoreRepositoryCustom{
    fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto>
}

class DepotStoreRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DepotStoreRepositoryCustom{

    override fun findPage(pageable: Pageable, depotStoreQuery: DepotStoreQuery): Page<DepotStoreDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotStoreDto::class.java)

        return query.resultList as Page<DepotStoreDto>
    }
}
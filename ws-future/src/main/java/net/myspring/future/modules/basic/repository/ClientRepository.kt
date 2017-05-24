package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Client
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("client"))
interface ClientRepository :BaseRepository<Client,String> {

    @Cacheable
    override fun findOne(id: String): Client


    override fun findAll(): List<Client>

    @CachePut(key = "#id")
    fun save(client: Client): Int

    @Query("""
        SELECT t1.*
        FROM crm_client t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Client>

    @Query("""
        select t1.*
        from crm_client t1, crm_depot t2
        where
        t1.enabled=1
        and t1.id = t2.client_id
        and t2.id = ?1
    """, nativeQuery = true)
    fun findByDepotId(depotId: String): Client

    fun findByNameLike(name: String): List<Client>
}
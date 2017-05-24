package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Pricesystem
import net.myspring.future.modules.basic.dto.PricesystemDto
import net.myspring.future.modules.basic.web.query.PricesystemQuery
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("pricesystems"))
interface PricesystemRepository : BaseRepository<Pricesystem,String>{
    @Cacheable
    override fun findOne(id: String): Pricesystem

    override fun findAll(): List<Pricesystem>

    @CachePut(key = "#id")
    fun save(pricesystem: Pricesystem): Int

    @Query("""
        SELECT t1.*
        FROM crm_pricesystem t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Pricesystem>

    fun findByIds(ids: List<String>): List<Pricesystem>
}
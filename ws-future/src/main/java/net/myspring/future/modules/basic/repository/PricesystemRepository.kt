package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Pricesystem
import net.myspring.future.modules.basic.dto.PricesystemDto
import net.myspring.future.modules.basic.web.query.PricesystemQuery
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
@CacheConfig(cacheNames = arrayOf("pricesystems"))
interface PricesystemRepository : BaseRepository<Pricesystem,String>,PricesystemRepositoryCustom {
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

    @Query("""
        SELECT t1.*
        FROM crm_pricesystem t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<Pricesystem>

    @Query("""
        SELECT t1.*
        FROM crm_pricesystem t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findPricesystem(): List<Pricesystem>
}

interface PricesystemRepositoryCustom{
    fun findPage(pageable: Pageable, pricesystemQuery: PricesystemQuery): Page<PricesystemDto>
}

class PricesystemRepositoryImpl @Autowired constructor(val entityManager: EntityManager):PricesystemRepositoryCustom{

    override fun findPage(pageable: Pageable, pricesystemQuery: PricesystemQuery): Page<PricesystemDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), PricesystemDto::class.java)

        return query.resultList as Page<PricesystemDto>
    }
}
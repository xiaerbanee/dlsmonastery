package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Depot
import net.myspring.future.modules.basic.dto.DepotAccountDto
import net.myspring.future.modules.basic.dto.DepotDto
import net.myspring.future.modules.basic.web.query.DepotAccountQuery
import net.myspring.future.modules.basic.web.query.DepotQuery
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
@CacheConfig(cacheNames = arrayOf("depots"))
interface DepotRepository :BaseRepository<Depot,String>,DepotRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): Depot

    override fun findAll(): List<Depot>

    @CachePut(key = "#id")
    fun save(depot: Depot): Int

    @Query("""
        SELECT t1.*
        FROM crm_depot t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<Depot>

    @Query("""
        SELECT t1.*
        FROM crm_depot t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<Depot>

    fun findByChainId(chainId: String): List<Depot>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_depot t1,
            crm_account_depot t2
        WHERE
            t1.enabled = 1
        AND t1.is_hidden = 0
        AND t1.id = t2.depot_id
        AND t2.account_id = ?1
    """, nativeQuery = true)
    fun findByAccountId(accountId: String): List<Depot>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_depot t1
        WHERE
            t1.enabled = 1
        AND t1.name IN ?1
    """, nativeQuery = true)
    fun findByNameList(nameList: List<String>): List<Depot>

    fun findByName(name: String): Depot

}

interface DepotRepositoryCustom{

    fun findShopList(depotShopQuery: DepotQuery): List<DepotDto>

    fun findStoreList(depotShopQuery: DepotQuery): List<DepotDto>

    fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto>

    fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,boolean: Boolean):Page<DepotAccountDto>
}

class DepotRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DepotRepositoryCustom{

    override fun findShopList(depotStoreQuery: DepotQuery): List<DepotDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as List<DepotDto>
    }

    override fun findStoreList(depotShopQuery: DepotQuery): List<DepotDto>{
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as List<DepotDto>
    }

    override fun findPage(pageable: Pageable, depotQuery: DepotQuery): Page<DepotDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotDto::class.java)

        return query.resultList as Page<DepotDto>
    }

    override fun findDepotAccountList(pageable: Pageable,depotAccountQuery: DepotAccountQuery,boolean: Boolean):Page<DepotAccountDto>{
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DepotAccountDto::class.java)

        return query.resultList as Page<DepotAccountDto>
    }
}
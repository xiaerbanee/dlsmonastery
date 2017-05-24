package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.Depot
import net.myspring.future.modules.basic.dto.DepotDto
import net.myspring.future.modules.basic.web.query.DepotQuery
import org.apache.ibatis.annotations.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("depots"))
interface DepotRepository :BaseRepository<Depot,String> {

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

    fun findByIds(ids: List<String>): List<Depot>

    fun findShopList(depotShopQuery: DepotQuery): List<DepotDto>

    fun findStoreList(depotShopQuery: DepotQuery): List<DepotDto>

    fun findByChainId(chainId: String): List<Depot>

    fun findByAccountId(accountId: String): List<Depot>

    fun findByNameList(nameList: List<String>): List<Depot>

    fun findByName(name: String): Depot

    fun findShopByGoodsOrderId(goodsOrderId: String): DepotDto

    fun findStoreByGoodsOrderId(goodsOrderId: String): DepotDto
}
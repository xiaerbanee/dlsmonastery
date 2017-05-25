package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.ShopAdType
import net.myspring.future.modules.basic.dto.ShopAdTypeDto
import net.myspring.future.modules.basic.web.query.ShopAdTypeQuery
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
@CacheConfig(cacheNames = arrayOf("shopAdTypes"))
interface ShopAdTypeRepository : BaseRepository<ShopAdType,String> {

    @Cacheable
    override fun findOne(id: String): ShopAdType

    override fun findAll(): List<ShopAdType>

    @CachePut(key = "#id")
    fun save(shopAdType: ShopAdType): Int

    @Query("""
        SELECT t1.*
        FROM crm_shop_ad_type t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<ShopAdType>

    fun findAllByEnabled(): List<ShopAdTypeDto>

    @Query("""
        SELECT t1.*
        FROM crm_shop_ad_type t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<ShopAdType>
}
package net.myspring.future.modules.basic.repository

import net.myspring.common.cache.IdCacheKeyGenerator
import net.myspring.future.common.mybatis.MyProvider
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneType
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery
import org.apache.ibatis.annotations.*
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("demoPhoneTypes"))
interface DemoPhoneTypeRepository : BaseRepository<DemoPhoneType,String> {

    @Cacheable
    override fun findOne(id: String): DemoPhoneType

    override fun findAll(): List<DemoPhoneType>

    @CachePut(key = "#id")
    fun save(demoPhoneType: DemoPhoneType): Int

    @Query("""
        SELECT t1.*
        FROM crm_demo_phone_type t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): List<DemoPhoneType>


    fun findByIds(ids: List<String>): List<DemoPhoneType>

    fun findAllByApplyEndDate(applyEndDate: LocalDate): List<DemoPhoneType>
}
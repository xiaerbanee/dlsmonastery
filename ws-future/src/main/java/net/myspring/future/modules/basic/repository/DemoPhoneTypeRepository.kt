package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneType
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("demoPhoneTypes"))
interface DemoPhoneTypeRepository : BaseRepository<DemoPhoneType,String>,DemoPhoneTypeRepositoryCustom {

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

    @Query("""
        SELECT t1.*
        FROM crm_demo_phone_type t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<DemoPhoneType>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_demo_phone_type t1
        WHERE
            t1.enabled = 1
        AND t1.apply_end_date > ?1
    """, nativeQuery = true)
    fun findAllByApplyEndDate(applyEndDate: LocalDate): List<DemoPhoneType>
}

interface DemoPhoneTypeRepositoryCustom{
    fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto>
}

class DemoPhoneTypeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DemoPhoneTypeRepositoryCustom{

    override fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto> {
        val sb = StringBuffer()

        var query = entityManager.createNativeQuery(sb.toString(), DemoPhoneTypeDto::class.java)

        return query.resultList as Page<DemoPhoneTypeDto>
    }
}
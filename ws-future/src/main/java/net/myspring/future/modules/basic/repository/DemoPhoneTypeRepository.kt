package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneType
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery
import net.myspring.util.text.StringUtils
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

    override fun findAll(): MutableList<DemoPhoneType>

    @CachePut(key = "#id")
    fun save(demoPhoneType: DemoPhoneType): Int

    @Query("""
        SELECT t1.*
        FROM crm_demo_phone_type t1
        where t1.enabled=1
    """, nativeQuery = true)
    fun findAllEnabled(): MutableList<DemoPhoneType>

    @Query("""
        SELECT t1.*
        FROM crm_demo_phone_type t1
        where t1.enabled=1
        and t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: MutableList<String>): MutableList<DemoPhoneType>

    @Query("""
        SELECT
            t1.*
        FROM
            crm_demo_phone_type t1
        WHERE
            t1.enabled = 1
        AND t1.apply_end_date > ?1
    """, nativeQuery = true)
    fun findAllByApplyEndDate(applyEndDate: LocalDate): MutableList<DemoPhoneType>
}

interface DemoPhoneTypeRepositoryCustom{
    fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto>
}

class DemoPhoneTypeRepositoryImpl @Autowired constructor(val entityManager: EntityManager):DemoPhoneTypeRepositoryCustom{

    override fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto> {
        val sb = StringBuffer()
        sb.append("""
            SELECT
                t1.*, GROUP_CONCAT(DISTINCT t2. NAME) AS productTypeNames
            FROM
                crm_demo_phone_type t1
            LEFT JOIN crm_product_type t2 ON t1.id = t2.demo_phone_type_id
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(demoPhoneTypeQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }
        sb.append(""" GROUP BY t1.id """)
        var query = entityManager.createNativeQuery(sb.toString(), DemoPhoneTypeDto::class.java)

        return query.resultList as Page<DemoPhoneTypeDto>
    }
}
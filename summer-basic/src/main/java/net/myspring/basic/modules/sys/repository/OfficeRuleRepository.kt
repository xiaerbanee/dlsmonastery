package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.OfficeRule
import net.myspring.basic.modules.sys.dto.OfficeRuleDto
import net.myspring.basic.modules.sys.dto.PermissionDto
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery
import net.myspring.basic.modules.sys.web.query.PermissionQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */

interface OfficeRuleRepository  : BaseRepository<OfficeRule, String> ,OfficeRuleRepositoryCustom{

    @Cacheable
    override fun findOne(id: String): OfficeRule

    @CachePut(key="#id")
    fun save(officeRule: OfficeRule): Int

    @Query("""
        SELECT t1.*
        FROM sys_office_rule t1
        where t1.enabled=1
        and t1.name=?1
     """, nativeQuery = true)
    fun findByName(name: String): OfficeRule

    @Query("""
            SELECT t1.*
        FROM sys_office_rule t1
        where t1.enabled=1
        and t1.type=?1
     """, nativeQuery = true)
    fun findByType(type: String): MutableList<OfficeRule>

    @Query("""
            SELECT t1.*
        FROM sys_office_rule t1
        where t1.enabled=1
        order BY  t1.level asc
        limit 0,1
     """, nativeQuery = true)
    fun findTopOfficeRule(): OfficeRule

    @Query("""
            SELECT t1.*
        FROM sys_office_rule t1
        where t1.enabled=1
        and t1.level > (
          SELECT level FROM sys_office_rule where id=?1
        )
        limit 0,1
     """, nativeQuery = true)
    fun findNextOfficeRule(id: String): OfficeRule

    @Query("""
    SELECT t1.*
        FROM sys_office_rule t1
        where t1.enabled=1
     """, nativeQuery = true)
    fun findAllEnabled():MutableList<OfficeRule>
}


interface OfficeRuleRepositoryCustom{

    fun findPage(pageable: Pageable, officeRuleQuery: OfficeRuleQuery): Page<OfficeRuleDto>?


}

class OfficeRuleRepositoryImpl @Autowired constructor(val entityManager: EntityManager): OfficeRuleRepositoryCustom{
    override fun findPage(pageable: Pageable, officeRuleQuery: OfficeRuleQuery): Page<OfficeRuleDto>? {
        return null
    }


}
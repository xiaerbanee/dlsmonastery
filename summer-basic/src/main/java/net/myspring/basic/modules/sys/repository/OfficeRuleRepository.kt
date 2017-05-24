package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.OfficeRule
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */

interface OfficeRuleRepository  {

    @Cacheable
    fun findOne(id: String): OfficeRule

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
    fun findByType(type: String): List<OfficeRule>

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
}

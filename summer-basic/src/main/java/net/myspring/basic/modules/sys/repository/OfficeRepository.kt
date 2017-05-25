package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Office
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import net.myspring.basic.modules.sys.web.query.OfficeQuery
import net.myspring.basic.modules.sys.dto.OfficeDto
import org.springframework.data.repository.query.Param


/**
 * Created by haos on 2017/5/24.
 */
interface OfficeRepository :BaseRepository<Office,String>{
    @CachePut(key="#id")
    fun save(office: Office): Office

    @Cacheable
    override fun findOne(id: String): Office

    @Query("""
     SELECT t1.*
        FROM sys_office t1,sys_office_rule t2
        where t1.office_rule_id=t2.id
        and t2.name=?1
        and t1.enabled =1
     """, nativeQuery = true)
    fun findByOfficeRuleName(officeRuleName:String):List<Office>

    @Query("""
    SELECT t1.*
        FROM sys_office t1,hr_account t3
        where t1.enabled=1
        and t1.id=t3.office_id
        and t3.id=?1
     """, nativeQuery = true)
    fun findByAccountId(accountId:String):Office

    @Query("""
     SELECT t1.*
        FROM sys_office t1,hr_account t3
        where t1.enabled=1
        and t1.id=t3.office_id
        and t3.account_id in ?1
     """, nativeQuery = true)
    fun findByAccountIds(accountIds:List<String>):List<Office>

    @Query("""
        SELECT t1.*
        FROM sys_office t1
        where t1.parent_id is NULL
     """, nativeQuery = true)
    fun findParentIdIsNull(): Office

    @Query("""
       SELECT t1.*
        FROM sys_office t1
        where t1.parent_ids like %?1%
        and t1.enabled =1
     """, nativeQuery = true)
    fun findByParentIdsLike(parentId: String): List<Office>

    @Query("""

     """, nativeQuery = true)
    fun findByParentIdsListLike(parentIdList: List<String>): List<Office>

    @Query("""

     """, nativeQuery = true)
    fun findByFilter(@Param("p") officeQuery: OfficeQuery): List<Office>

    @Query("""

     """, nativeQuery = true)
    fun findByFilterAll(@Param("p") map: Map<String, Any>): List<Office>

    @Query("""
       SELECT t1.id,t1.name
        FROM sys_office t1
        WHERE t1.enabled = 1
        and t1.id in ?1
     """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<Office>

    @Query("""

     """, nativeQuery = true)
    fun findByAreaIds(areaIds: List<String>): List<Office>

    @Query("""
        SELECT t1.*
        FROM sys_office t1,sys_office_rule t2
        where t1.enabled=1
        and t2.enabled=1
        and t1.office_rule_id=t2.id
        and (t1.parent_ids like %?1% or t1.id=?2)
        and t1.office_rule_id=?2
     """, nativeQuery = true)
    fun findByOfficeIdAndRuleId( officeId: String, officeRuleId: String): Office

    @Query("""
       SELECT t1.*
        FROM sys_office t1
        WHERE t1.enabled=1
        and t1.area_id  = (
            SELECT
            t2.area_id
            FROM
            sys_office t2
            WHERE
            t2.id = ?1
         )
     """, nativeQuery = true)
    fun findSameAreaByOfficeId(officeId: String): List<Office>

}
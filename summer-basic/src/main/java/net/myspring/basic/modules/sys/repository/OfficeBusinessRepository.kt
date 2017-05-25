package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.OfficeLeader
import net.myspring.basic.modules.sys.domain.OfficeBusiness
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  OfficeBusinessRepository: BaseRepository<OfficeBusiness, String> {
    @CachePut(key="#id")
    fun save(officeBusiness: OfficeBusiness): OfficeBusiness

    @Cacheable
    override  fun findOne(id: String): OfficeBusiness

    @Query("""
        SELECT  t1.*
        FROM sys_office_business t1
        where t1.enabled=1
        and t1.office_id=?1
     """, nativeQuery = true)
    fun findBusinessIdById(id:String):List<OfficeBusiness>

    @Query("""
         SELECT  t1.*
        FROM sys_office_business t1
        where  t1.office_id=?1
     """, nativeQuery = true)
    fun findAllBusinessIdById(id:String):List<OfficeBusiness>

    @Query("""
             UPDATE  sys_office_business set enabled=0 where business_office_id IN ?1
     """, nativeQuery = true)
    fun setEnabledByBusinessOfficeIds(businessOfficeIds:List<String>):Int

    @Query("""
            UPDATE  sys_office_business
            set enabled=?1
            where office_id=?2
     """, nativeQuery = true)
    fun setEnabledByOfficeId(enabled:Boolean,officeId:String ):Int

    @Query("""
            UPDATE  sys_office_business
            set enabled=?1
            where office_id=?2
     """, nativeQuery = true)
    //TODO 修改
    fun batchSave(officeBusinessList:List<OfficeBusiness>)
}
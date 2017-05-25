package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Office
import net.myspring.basic.modules.sys.dto.OfficeDto
import net.myspring.basic.modules.sys.web.query.OfficeQuery
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.EntityManager


/**
 * Created by haos on 2017/5/24.
 */
interface OfficeRepository :BaseRepository<Office,String>,OfficeRepositoryCustom{
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
       SELECT t1.id,t1.name
        FROM sys_office t1
        WHERE t1.enabled = 1
        and t1.id in ?1
     """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<Office>

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



    @Query("""
       SELECT t1.*
        FROM sys_office t1
        WHERE t1.enabled=1
     """, nativeQuery = true)
            //TODO 修改
    fun findAllEnabled():List<Office>
}

interface OfficeRepositoryCustom {
    fun findByParentIdsListLike(parentIdList: List<String>): List<Office>

    fun findByFilter(officeQuery: OfficeQuery): List<Office>

    fun findByFilterAll(@Param("p") map: Map<String, Any>): List<Office>

    fun findByAreaIds(areaIds: List<String>): List<Office>

    fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>?
}

class OfficeRepositoryImpl@Autowired constructor(val entityManager: EntityManager): OfficeRepositoryCustom {
    override fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>? {
        return null;
    }

    override fun findByParentIdsListLike(parentIdList: List<String>): List<Office> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1
            where  t1.enabled =1
            and (
        """)
        for((index,value) in parentIdList.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if(index < parentIdList.size-1) {
                sb.append(" or ");
            }
        }
        var query = entityManager.createNativeQuery(sb.toString());
        for((index,value) in parentIdList.withIndex()) {
            query.setParameter("parentId" + index ,"%$value%");
        }
        return query.resultList as List<Office>;
    }

    override fun findByFilter(officeQuery: OfficeQuery): List<Office> {
        var sb = StringBuilder();
        sb.append("""
            select office.*
            from
            sys_office office
            where
            office.enabled=1
        """)
        if(StringUtils.isNotBlank(officeQuery.name)) {
            sb.append("  and office.name like %:name%")
        }
        if(StringUtils.isNotBlank(officeQuery.id)) {
            sb.append(" and office.name = :id")
        }
        sb.append("""
            order by office.name
            limit 0,20
        """)
        var query = entityManager.createNativeQuery(sb.toString());
        QueryUtils.setParameter(query,officeQuery);
        return query.resultList as List<Office>;
    }

    override fun findByFilterAll(map: Map<String, Any>): List<Office> {
        var sb = StringBuilder();
        sb.append("""
            select office.*
            from
            sys_office office
            where
            office.enabled=1
        """)
        var query = entityManager.createNativeQuery(sb.toString());
        return query.resultList as List<Office>;
    }

    override fun findByAreaIds(areaIds: List<String>): List<Office> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1
            where t1.enabled=1
            and (
            t1.id IN :areaIds or
        """)

        for((index,value) in areaIds.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if(index < areaIds.size-1) {
                sb.append(" or ");
            }
        }
        var query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("areaIds",areaIds);
        for((index,value) in areaIds.withIndex()) {
            query.setParameter("parentId" + index ,"%$value%");
        }
        return query.resultList as List<Office>;
    }

}

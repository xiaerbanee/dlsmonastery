package net.myspring.basic.modules.sys.repository

import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Office
import net.myspring.basic.modules.sys.dto.OfficeDto
import net.myspring.basic.modules.sys.web.query.OfficeQuery
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("offices"))
interface OfficeRepository :BaseRepository<Office,String>,OfficeRepositoryCustom{
    @CachePut(key="#id")
    fun save(office: Office): Office

    @Cacheable
    override fun findOne(id: String): Office

    @Query("""
        SELECT t1
        FROM  #{#entityName} t1,OfficeRule t2
        where t1.officeRuleId=t2.id
        and t2.name=?1
        and t1.enabled =1
     """)
    fun findByOfficeRuleName(officeRuleName:String):MutableList<Office>

    @Query("""
        SELECT t1
        FROM  #{#entityName} t1,Account t2
        where t1.enabled=1
        and t1.id=t2.officeId
        and t2.id=?1
     """)
    fun findByAccountId(accountId:String):Office

    @Query("""
        SELECT t1
        FROM  #{#entityName} t1,Account t2
        where t1.enabled=1
        and t1.id=t2.officeId
        and t2.id in ?1
     """)
    fun findByAccountIds(accountIds:MutableList<String>):MutableList<Office>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.parentId is NULL
     """)
    fun findParentIdIsNull(): Office

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.parentIds like %?1%
        and t.enabled =1
     """)
    fun findByParentIdsLike(parentId: String): MutableList<Office>

    @Query("""
        SELECT t.id,t.name
        FROM  #{#entityName} t
        WHERE t.enabled = 1
        and t.id in ?1
     """)
    fun findByIds(ids: MutableList<String>): MutableList<Office>

    @Query("""
        SELECT t1
        FROM  #{#entityName} t1,OfficeRule t2
        where t1.enabled=1
        and t2.enabled=1
        and t1.officeRuleId=t2.id
        and (t1.parentIds like %?1% or t1.id=?2)
        and t1.officeRuleId=?2
     """)
    fun findByOfficeIdAndRuleId( officeId: String, officeRuleId: String): Office

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE t.enabled=1
        and t.areaId  = (
            SELECT
            t2.areaId
            FROM
            Office t2
            WHERE
            t2.id = ?1
         )
     """)
    fun findSameAreaByOfficeId(officeId: String): MutableList<Office>



    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE t.enabled=1
     """)
    fun findAllEnabled():MutableList<Office>
}

interface OfficeRepositoryCustom {
    fun findByParentIdsListLike(parentIdList: MutableList<String>): MutableList<Office>

    fun findByFilter(officeQuery: OfficeQuery): MutableList<Office>

    fun findByFilterAll(map: Map<String, Any>): MutableList<Office>

    fun findByAreaIds(areaIds: MutableList<String>): MutableList<Office>

    fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>?
}

class OfficeRepositoryImpl@Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): OfficeRepositoryCustom {
    override fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>? {
        return null;
    }

    override fun findByParentIdsListLike(parentIdList: MutableList<String>): MutableList<Office> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1
            where  t1.enabled =1
            and (
        """)
        for((index) in parentIdList.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if(index < parentIdList.size-1) {
                sb.append(" or ");
            }
        }
        var paramMap = Maps.newHashMap<String,String>();
        for((index,value) in parentIdList.withIndex()) {
            paramMap.put("parentId" + index ,"%$value%");
        }
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap, BeanPropertyRowMapper(Office::class.java))
    }

    override fun findByFilter(officeQuery: OfficeQuery): MutableList<Office> {
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
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(officeQuery), BeanPropertyRowMapper(Office::class.java))
    }

    override fun findByFilterAll(map: Map<String, Any>): MutableList<Office> {
        var sb = StringBuilder();
        sb.append("""
            select office.*
            from
            sys_office office
            where
            office.enabled=1
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertyRowMapper(Office::class.java))
    }

    override fun findByAreaIds(areaIds: MutableList<String>): MutableList<Office> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1
            where t1.enabled=1
            and (
            t1.id IN :areaIds or
        """)
        for((index) in areaIds.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if(index < areaIds.size-1) {
                sb.append(" or ");
            }
        }
        var paramMap = Maps.newHashMap<String,Any>();
        paramMap.put("areaIds",areaIds);
        for((index,value) in areaIds.withIndex()) {
            paramMap.put("parentId" + index ,"%$value%");
        }
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap, BeanPropertyRowMapper(Office::class.java));
    }

}

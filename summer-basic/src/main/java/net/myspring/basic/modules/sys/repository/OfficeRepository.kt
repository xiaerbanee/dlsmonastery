package net.myspring.basic.modules.sys.repository

import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Office
import net.myspring.basic.modules.sys.dto.DictMapDto
import net.myspring.basic.modules.sys.dto.OfficeChildDto
import net.myspring.basic.modules.sys.dto.OfficeDto
import net.myspring.basic.modules.sys.web.query.OfficeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("offices"))
interface OfficeRepository :BaseRepository<Office,String>,OfficeRepositoryCustom{
    @CachePut(key="#p0.id")
    fun save(office: Office): Office

    fun findByEnabledIsTrueAndParentId(parentId: String):MutableList<Office>

    @Cacheable
    override fun findOne(id: String): Office

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
        where t.parentIds like ?1
        and t.enabled =1
     """)
    fun findByParentIdsLike(parentId: String): MutableList<Office>

    fun findByEnabledIsTrueAndIdIn(ids: MutableList<String>): MutableList<Office>

    @Query("""
        SELECT t1
        FROM  #{#entityName} t1,OfficeRule t2
        where t1.enabled=1
        and t2.enabled=1
        and t1.officeRuleId=t2.id
        and (t1.parentIds like ?1 or t1.id=?1)
        and t1.officeRuleId=?2
     """)
    fun findByOfficeIdAndRuleId( officeId: String, officeRuleId: String): MutableList<Office>

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

    fun findByEnabledIsTrueAndType(type:String):MutableList<Office>
}

interface OfficeRepositoryCustom {
    fun findByOfficeRuleName(officeRuleName:String):MutableList<Office>

    fun findByParentIdsListLike(parentIdList: MutableList<String>): MutableList<Office>

    fun findByParentIdsListLikeAndOfficeRuleId(parentIdList: MutableList<String>,officeRuleId: String): MutableList<Office>

    fun findByFilter(officeQuery: OfficeQuery): MutableList<Office>

    fun findByFilterAll(map: Map<String, Any>): MutableList<Office>

    fun findByAreaIds(areaIds: MutableList<String>): MutableList<Office>

    fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>?

    fun findAllChildCount(): MutableList<OfficeChildDto>
}

class OfficeRepositoryImpl@Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): OfficeRepositoryCustom {
    override fun findByParentIdsListLikeAndOfficeRuleId(parentIdList: MutableList<String>, officeRuleId: String): MutableList<Office> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
            FROM sys_office t1,sys_office_rule t2
            where  t1.enabled =1
            and t1.office_rule_id=t2.id
            and t2.id=:officeRuleId
            and (
        """)
        for((index) in parentIdList.withIndex()) {
            sb.append(" t1.parent_ids like :parentId").append(index);
            if(index < parentIdList.size-1) {
                sb.append(" or ");
            }
        }
        sb.append(")");
        var paramMap = Maps.newHashMap<String,String>();
        for((index,value) in parentIdList.withIndex()) {
            paramMap.put("parentId" + index ,"%,$value,%");
        }
        paramMap.put("officeRuleId",officeRuleId);
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap, BeanPropertyRowMapper(Office::class.java))
    }

    override fun findByOfficeRuleName(officeRuleName:String):MutableList<Office>{
        return namedParameterJdbcTemplate.query("""
          SELECT t1.*
          FROM  sys_office t1,sys_office_rule t2
          where t1.office_rule_id=t2.id
          and t2.name = :officeRuleName
          and t1.enabled =1
          ORDER BY t1.task_point DESC
        """,Collections.singletonMap("officeRuleName",officeRuleName),BeanPropertyRowMapper(Office::class.java))
    }
    override fun findPage(pageable: Pageable, officeQuery: OfficeQuery): Page<OfficeDto>? {
        var sb = StringBuilder("select * from sys_office where enabled=1 ");
        if(StringUtils.isNotBlank(officeQuery.name)) {
            sb.append(" and name like concat('%',:name,'%')");
        }
        if(CollectionUtil.isNotEmpty(officeQuery.officeIdList)) {
            sb.append(" and id IN (:officeIdList)");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(officeQuery), BeanPropertyRowMapper(OfficeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(officeQuery),Long::class.java);
        return PageImpl(list,pageable,count);
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
        sb.append(")");
        var paramMap = Maps.newHashMap<String,String>();
        for((index,value) in parentIdList.withIndex()) {
            paramMap.put("parentId" + index ,"%,$value,%");
        }
        print(sb.toString())
        print(paramMap)
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
            sb.append("  and office.name like concat('%',:name,'%')")
        }
        if(StringUtils.isNotBlank(officeQuery.id)) {
            sb.append(" and office.name = :id")
        }
        if(CollectionUtil.isNotEmpty(officeQuery.officeIdList)) {
            sb.append(" and id IN (:officeIdList)");
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

    override fun findAllChildCount():MutableList<OfficeChildDto> {
        return namedParameterJdbcTemplate.query("""
            select
                of.*,
                count(*) as childCount
            from
                hr_office of,
                hr_office of1
            where
                of1.parent_ids like concat('%,', of.id, ',%')
            group by
                of.id
            order by of.id asc
        """,BeanPropertyRowMapper(OfficeChildDto::class.java));
    }

}

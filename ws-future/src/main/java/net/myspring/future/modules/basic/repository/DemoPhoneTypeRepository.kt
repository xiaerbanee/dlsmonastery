package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneType
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery
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

    @CachePut(key = "#p0.id")
    fun save(demoPhoneType: DemoPhoneType): DemoPhoneType

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
    """)
    fun findAllEnabled(): MutableList<DemoPhoneType>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.id in ?1
    """)
    fun findByIds(ids: MutableList<String>): MutableList<DemoPhoneType>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
        AND t1.name LIKE CONCAT('%' ,?1, '%')
    """)
    fun findByNameLike(name: String):MutableList<DemoPhoneType>

    @Query("""
        SELECT
            t1
        FROM
            #{#entityName} t1
        WHERE
            t1.enabled = 1
    """)
    /*AND t1.apply_end_date > ?1*/
    fun findAllByApplyEndDate(applyEndDate: LocalDate): MutableList<DemoPhoneType>
}

interface DemoPhoneTypeRepositoryCustom{
    fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto>
}

class DemoPhoneTypeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DemoPhoneTypeRepositoryCustom{

    override fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeQuery): Page<DemoPhoneTypeDto> {
        val sb = StringBuilder("""
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
        if (StringUtils.isNotEmpty(demoPhoneTypeQuery.productTypeName)) {
            sb.append("""  and t2.name LIKE CONCAT('%',:productTypeName,'%') """)
        }
        sb.append(""" GROUP BY t1.id """)

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(demoPhoneTypeQuery), BeanPropertyRowMapper(DemoPhoneTypeDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(demoPhoneTypeQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
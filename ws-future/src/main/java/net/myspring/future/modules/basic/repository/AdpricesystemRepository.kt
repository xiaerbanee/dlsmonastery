package net.myspring.future.modules.basic.repository


import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.AdPricesystem
import net.myspring.future.modules.basic.dto.AdPricesystemDto
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery
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
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

//import kotlin.collections.HashMap

/**
 * Created by zhangyf on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("adPricesystems"))
interface AdpricesystemRepository : BaseRepository<AdPricesystem,String>,AdpricesystemRepositoryCustom {

    @Cacheable
    override fun findOne(id: String): AdPricesystem

    override fun findAll(): MutableList<AdPricesystem>

    @CachePut(key = "#p0.id")
    fun save(adPricesystem: AdPricesystem): AdPricesystem


    fun findByEnabledIsTrue(): MutableList<AdPricesystem>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        WHERE t1.enabled = 1
    """)
    fun findList(adPricesystemQuery: AdPricesystemQuery): MutableList<AdPricesystem>

    fun findByName(name: String): AdPricesystem


}

interface AdpricesystemRepositoryCustom{

    fun findOfficeById(id: String): MutableList<String>

    fun saveAdpricesystemOffice(adPricesystemId:String,officeId:MutableList<String>):Int

    fun deleteOfficeId(id: String):Int

    fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto>
}

class AdpricesystemRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdpricesystemRepositoryCustom{

    override fun findOfficeById(id: String): MutableList<String>{
        return namedParameterJdbcTemplate.queryForList("""
            SELECT
                t1.office_id
            FROM
                crm_ad_pricesystem_office t1
            WHERE
                t1.ad_pricesystem_id = :id
        """,Collections.singletonMap("id",id),String::class.java)
    }

    override fun saveAdpricesystemOffice(adPricesystemId:String,officeIdList:MutableList<String>):Int{
        val sb = StringBuilder("""
            INSERT INTO crm_ad_pricesystem_office (ad_pricesystem_id,office_id) VALUES
        """)
        for(officeId in officeIdList){
            sb.append("("+adPricesystemId+","+officeId+"),")
        }
        sb.deleteCharAt(sb.length-1)
        return namedParameterJdbcTemplate.update(sb.toString(),HashMap<String,Any>())
    }

    override fun deleteOfficeId(id: String):Int{
        return namedParameterJdbcTemplate.update("""
             DELETE FROM crm_ad_pricesystem_office where ad_pricesystem_id = :id
        """,Collections.singletonMap("id",id))
    }

    override fun findPage(pageable: Pageable, adPricesystemQuery: AdPricesystemQuery): Page<AdPricesystemDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(adPricesystemQuery.name)) {
            sb.append("""  and t1.name LIKE CONCAT('%',:name,'%') """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(adPricesystemQuery), BeanPropertyRowMapper(AdPricesystemDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(adPricesystemQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}


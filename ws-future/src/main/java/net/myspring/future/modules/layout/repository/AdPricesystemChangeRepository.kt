package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdPricesystemChange
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdPricesystemChangeRepository : BaseRepository<AdPricesystemChange,String>,AdPricesystemChangeRepositoryCustom{

}

interface AdPricesystemChangeRepositoryCustom{
    fun findPage(pageable: Pageable,adPricesystemChangeQuery: AdPricesystemChangeQuery): Page<AdPricesystemChangeDto>

    fun findFilter(adPricesystemChangeQuery: AdPricesystemChangeQuery): MutableList<AdPricesystemChangeDto>
}

class AdPricesystemChangeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdPricesystemChangeRepositoryCustom{

    override fun findPage(pageable: Pageable,adPricesystemChangeQuery: AdPricesystemChangeQuery): Page<AdPricesystemChangeDto>{
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem_change t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(adPricesystemChangeQuery.productId)) {
            sb.append("""  and t1.product_id = :productId """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(adPricesystemChangeQuery), BeanPropertyRowMapper(AdPricesystemChangeDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(adPricesystemChangeQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }

    override fun findFilter(adPricesystemChangeQuery: AdPricesystemChangeQuery): MutableList<AdPricesystemChangeDto>{
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_ad_pricesystem_change t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(adPricesystemChangeQuery.productId)) {
            sb.append("""  and t1.product_id = :productId """)
        }


        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(adPricesystemChangeQuery), BeanPropertyRowMapper(AdPricesystemChangeDto::class.java))
    }
}
package net.myspring.future.modules.crm.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChange
import net.myspring.future.modules.crm.domain.PriceChangeProduct

import net.myspring.future.modules.crm.domain.PricesystemChange
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.dto.ProductImeDto
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils

import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import org.apache.poi.ss.formula.functions.T
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager


interface PriceChangeRepository : BaseRepository<PriceChange, String>, PriceChangeRepositoryCustom {


    fun findByEnabledIsTrueAndUploadEndDateGreaterThanEqualOrderByIdDesc(uploadEndDate: LocalDateTime): MutableList<PriceChange>


    @Query("""
        select t
        from #{#entityName} t
        where t.enabled = 1
        and t.status = :status
    """)
    fun findByPriceChangeIme(@Param("status")status:String):MutableList<PriceChange>


    fun findByEnabledIsTrueOrderByIdDesc(): MutableList<PriceChange>

}

interface PriceChangeRepositoryCustom{
    fun findPage(pageable: Pageable, priceChangeQuery : PriceChangeQuery): Page<PriceChangeDto>

    fun findNearPriceChange(): PriceChange
}

class PriceChangeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PriceChangeRepositoryCustom {
    override fun findNearPriceChange(): PriceChange {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT t1
        FROM crm_price_change t1
        ORDER BY  t1.price_change_date DESC ,t1.upload_end_date DESC
        limit 0,1
                """, HashMap<String, Object>(), MyBeanPropertyRowMapper(PriceChange::class.java))
    }

    override fun findPage(pageable: Pageable, priceChangeQuery: PriceChangeQuery): Page<PriceChangeDto> {
        val sb = StringBuilder("""
         SELECT
            group_concat(DISTINCT productType.name) productTypeName, t1.*
        FROM
            crm_price_change t1,crm_price_change_product product,crm_product_type productType
        WHERE
            t1.enabled=1
            AND product.product_type_id = productType.id
            AND product.price_change_id = t1.id
        """)
        if (StringUtils.isNotEmpty(priceChangeQuery.name)) {
            sb.append("""  AND t1.name LIKE CONCAT('%', :name, '%')  """)
        }
        sb.append("""  GROUP BY product.price_change_id  """)

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(priceChangeQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(PriceChangeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }


}
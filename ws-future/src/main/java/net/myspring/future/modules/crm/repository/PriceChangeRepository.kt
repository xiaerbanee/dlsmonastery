package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.PriceChange

import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils

import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


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

    fun findProductTypeIdsById(id:String):MutableList<String>
}

class PriceChangeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): PriceChangeRepositoryCustom {
    override fun findNearPriceChange(): PriceChange {
        return namedParameterJdbcTemplate.queryForObject("""
        SELECT t1
        FROM crm_price_change t1
        ORDER BY  t1.price_change_date DESC ,t1.upload_end_date DESC
        limit 0,1
                """, HashMap<String, Any>(), BeanPropertyRowMapper(PriceChange::class.java))
    }

    override fun findPage(pageable: Pageable, priceChangeQuery: PriceChangeQuery): Page<PriceChangeDto> {
        val sb = StringBuilder("""
         SELECT
            group_concat(DISTINCT product.product_type_id) productTypeIds, t1.*
        FROM
            crm_price_change t1
            LEFT JOIN crm_price_change_product product ON  t1.id = product.price_change_id
        WHERE
            t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(priceChangeQuery.name)) {
            sb.append("""  AND t1.name LIKE CONCAT('%', :name, '%')  """)
        }
        sb.append("""  GROUP BY t1.id  """)

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        var paramMap = BeanPropertySqlParameterSource(priceChangeQuery)
        var list = namedParameterJdbcTemplate.query(pageableSql,paramMap, BeanPropertyRowMapper(PriceChangeDto::class.java))
        var count = namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Long::class.java)
        return PageImpl(list,pageable,count)

    }

    override fun findProductTypeIdsById(id:String):MutableList<String>{
        val sb = StringBuilder("""
            SELECT
                t2.product_type_id
            FROM
                crm_price_change t1
                LEFT JOIN crm_price_change_product t2 ON t1.id = t2.price_change_id
            WHERE
                t1.enabled = 1
            AND t1.id = :id
        """)
        return namedParameterJdbcTemplate.queryForList(sb.toString(),Collections.singletonMap("id", id), String::class.java)
    }

}
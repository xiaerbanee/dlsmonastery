package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.dto.ProductImeReportDto
import net.myspring.report.modules.future.dto.ProductTypeDto
import net.myspring.report.modules.future.web.query.ReportQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.lang.StringBuilder
import java.util.*

@Component
class ProductTypeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByScoreType(scoreType:Boolean): MutableList<ProductTypeDto> {
        return namedParameterJdbcTemplate.query("""
            select t1.* from crm_product_type t1 where t1.enabled=1 and t1.score_type=:scoreType;
        """, Collections.singletonMap("scoreType",scoreType), BeanPropertyRowMapper(ProductTypeDto::class.java))
    }

    fun findByMonth(month: String): List<ProductTypeDto> {
        val sb = StringBuilder("""
                     SELECT
                            t1.*
                        FROM
                            crm_product_ime_upload t,
                            crm_product_type t1
                        WHERE
                            t.enabled =1
                            AND t1.enabled =1
                            AND t1.id = t.product_type_id
                            AND t.month=:month
                    """)
        return namedParameterJdbcTemplate.query(sb.toString(), Collections.singletonMap("month",month),BeanPropertyRowMapper(ProductTypeDto::class.java));
    }
}
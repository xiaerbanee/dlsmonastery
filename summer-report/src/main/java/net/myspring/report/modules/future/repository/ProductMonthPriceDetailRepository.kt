package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.dto.DepotDto
import net.myspring.report.modules.future.dto.ProductMonthPriceDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductMonthPriceDetailRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findByMonth(month: String): MutableList<ProductMonthPriceDetailDto> {
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.*
        FROM
            crm_product_month_price_detail t1,
            crm_product_month_price t2
        WHERE
            t2.enabled = 1
        AND t1.product_month_price_id = t2.id
        AND t2.month = :month
          """, Collections.singletonMap("month", month), BeanPropertyRowMapper(ProductMonthPriceDetailDto::class.java))
    }
}
package net.myspring.tool.modules.oppo.repository

import net.myspring.tool.common.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FutureProductRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findHasImeProduct(): MutableList<ProductDto> {
        return namedParameterJdbcTemplate.query("""
            SELECT
               t1.*
            FROM
               crm_product t1
            WHERE
                t1.enabled = 1
            AND t1.has_ime = 1
        """,BeanPropertyRowMapper(ProductDto::class.java))
    }



}
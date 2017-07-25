package net.myspring.tool.modules.future.repository

import net.myspring.tool.modules.future.dto.ProductDto
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

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


    fun findByNameLike(name:String): MutableList<ProductDto> {
        val sb = StringBuilder()
        sb.append("""
            SELECT
               t1.*
            FROM
               crm_product t1
            WHERE
                t1.enabled = 1
            AND t1.has_ime = 1
        """)
        if (StringUtils.isNotBlank(name)){
            sb.append(" AND t1.name LIKE CONCAT('%',:name,'%')")
        }
        return namedParameterJdbcTemplate.query(sb.toString(), Collections.singletonMap("name",name),BeanPropertyRowMapper(ProductDto::class.java))
    }

}
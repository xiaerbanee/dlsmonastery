package net.myspring.tool.modules.imoo.repository;

import net.myspring.tool.modules.imoo.dto.ImooProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class ImooProductMapRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
     fun findByImooPlantBasicProductId(): MutableList<ImooProductDto> {
        val sb = """
            SELECT
                t1.*, t2.segment1 as segment1
            FROM
                imoo_product_map t1,
                imoo_plant_basic_product t2
            WHERE
                t1.imoo_plant_basic_product_id = t2.id
        """
        return namedParameterJdbcTemplate.query(sb,BeanPropertyRowMapper(ImooProductDto::class.java))
    }
}

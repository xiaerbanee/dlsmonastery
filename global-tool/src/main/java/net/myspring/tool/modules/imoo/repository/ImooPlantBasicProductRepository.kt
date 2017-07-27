package net.myspring.tool.modules.imoo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class ImooPlantBasicProductRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findSegment1s(segment1s: MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>()
        paramMap.put("segment1s", segment1s)
        return namedParameterJdbcTemplate.query("""
            select t.segment1
            from imoo_plant_basic_product t
            where t.segment1 in (:segment1s)
            """, paramMap, BeanPropertyRowMapper(String::class.java))
    }

    fun batchSave(imooPlantBasicProductList:MutableList<ImooPlantBasicProduct>):IntArray{
        val sb = """ INSERT INTO imoo_plant_basic_product(segment1,description,product_id,title,plid,plname,created_time)
                values(:segment1,:description,:productId,:title,:plid,:plname,:createdTime)"""
        return namedParameterJdbcTemplate.batchUpdate(sb,SqlParameterSourceUtils.createBatch(imooPlantBasicProductList.toTypedArray()));

    }
}

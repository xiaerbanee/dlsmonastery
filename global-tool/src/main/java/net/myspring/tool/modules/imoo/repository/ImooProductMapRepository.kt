package net.myspring.tool.modules.imoo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.modules.imoo.dto.ImooProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
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

    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("delete from imoo_product_map where 1=1",map)
    }

    fun BatchSave(imooProductDtoList: MutableList<ImooProductDto>):IntArray{
        val sb = """
            INSERT INTO imoo_product_map(imoo_plant_basic_product_id,product_id,created_by,created_date,last_modified_by,last_modified_date,version,locked,enabled,company_id)
            VALUES (:imooPlantBasicProductId,:productId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,'0','0','1','1')
        """
        return namedParameterJdbcTemplate.batchUpdate(sb,SqlParameterSourceUtils.createBatch(imooProductDtoList.toTypedArray()))
    }
}

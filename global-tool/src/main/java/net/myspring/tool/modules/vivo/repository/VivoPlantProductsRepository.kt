package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface VivoPlantProductsRepository : BaseRepository<VivoPlantProducts, String>, VivoPlantProductsRepositoryCustom {

}

interface VivoPlantProductsRepositoryCustom{
    fun findPlantProducts() :MutableList<VivoPlantProducts>
    fun findItemNumbers(itemNumbers: MutableList<String>,companyName:String): MutableList<VivoPlantProducts>
    fun findAllByProductId():MutableList<VivoPlantProducts>
}

class VivoPlantProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantProductsRepositoryCustom {
    override fun findPlantProducts(): MutableList<VivoPlantProducts>{
        return namedParameterJdbcTemplate.query("""
            select * from VR_PlantProducts
        """,  BeanPropertyRowMapper(VivoPlantProducts::class.java));
    }

    override fun findItemNumbers(itemNumbers:MutableList<String>,companyName: String): MutableList<VivoPlantProducts>{
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("itemNumbers",itemNumbers);
        paramMap.put("companyName",companyName);
        return namedParameterJdbcTemplate.query("""
              select * from vivo_plant_products  t where t.item_number in(:itemNumbers) and t.company_name=:companyName
        """,  paramMap,BeanPropertyRowMapper(VivoPlantProducts::class.java));
    }

    override fun findAllByProductId(): MutableList<VivoPlantProducts> {
        val sql = "select * from vivo_plant_products t  where t.product_Id is not null or t.lx_Product_Id is not null";
        return namedParameterJdbcTemplate.query(sql,BeanPropertyRowMapper(VivoPlantProducts::class.java))
    }
}

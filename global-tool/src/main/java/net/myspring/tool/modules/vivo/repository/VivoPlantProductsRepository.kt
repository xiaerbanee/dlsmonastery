package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface VivoPlantProductsRepository : BaseRepository<VivoPlantProducts, String>, VivoPlantProductsRepositoryCustom {

}
interface VivoPlantProductsRepositoryCustom{
    fun findPlantProducts() :MutableList<VivoPlantProducts>
    fun findItemNumbers(itemNumbers: MutableList<String>): MutableList<String>

}
class VivoPlantProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantProductsRepositoryCustom {
    override fun findPlantProducts(): MutableList<VivoPlantProducts>{
        return namedParameterJdbcTemplate.query("""
       select *  from VR_PlantProducts
        """,  BeanPropertyRowMapper(VivoPlantProducts::class.java));
    }

    override fun findItemNumbers(itemNumbers:MutableList<String>): MutableList<String>{
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("itemNumbers",itemNumbers);
        return namedParameterJdbcTemplate.query("""
                select t.item_number  from vivo_plant_products  t where t.item_number in(:itemNumbers)
        """,  paramMap,BeanPropertyRowMapper(String::class.java));
    }
}

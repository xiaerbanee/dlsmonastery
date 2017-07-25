package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.common.enums.CompanyNameEnum
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import net.myspring.tool.modules.vivo.dto.VivoPlantProductsDto
import net.myspring.tool.modules.vivo.web.query.VivoPlantProductsQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface VivoPlantProductsRepository : BaseRepository<VivoPlantProducts, String>, VivoPlantProductsRepositoryCustom {

}

interface VivoPlantProductsRepositoryCustom{
    fun findPlantProducts(companyName:String) :MutableList<VivoPlantProducts>
    fun findItemNumbers(itemNumbers: MutableList<String>,companyName:String): MutableList<VivoPlantProducts>
    fun findAllByProductId():MutableList<VivoPlantProducts>
    fun findAll(vivoPlantProductsQuery: VivoPlantProductsQuery):MutableList<VivoPlantProductsDto>
}

class VivoPlantProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantProductsRepositoryCustom {
    override fun findPlantProducts(companyName: String): MutableList<VivoPlantProducts>{
        var sql = "select * from VR_PlantProducts";
        if(CompanyNameEnum.IDVIVO.name.equals(companyName)) {
            sql =" select * from vr_products_m13e00";
        }
        return namedParameterJdbcTemplate.query(sql,  BeanPropertyRowMapper(VivoPlantProducts::class.java));
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

    override fun findAll(vivoPlantProductsQuery: VivoPlantProductsQuery): MutableList<VivoPlantProductsDto> {
        val sb = StringBuilder()
        sb.append("SELECT * FROM vivo_plant_products WHERE 1=1")
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(vivoPlantProductsQuery),BeanPropertyRowMapper(VivoPlantProductsDto::class.java))
    }
}

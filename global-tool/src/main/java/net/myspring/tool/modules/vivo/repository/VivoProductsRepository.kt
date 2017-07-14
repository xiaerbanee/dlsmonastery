package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface VivoProductsRepository : BaseRepository<VivoProducts, String>, VivoProductsRepositoryCustom {

}
interface VivoProductsRepositoryCustom{
    fun findProducts() :MutableList<VivoProducts>
    fun findColorIds(colorIds:MutableList<String>):MutableList<VivoProducts>
}
class VivoProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : VivoProductsRepositoryCustom{
    override fun findProducts(): MutableList<VivoProducts>{
        return namedParameterJdbcTemplate.query("""
       select *  from VR_Products
        """,  BeanPropertyRowMapper(VivoProducts::class.java));
    }

    override fun findColorIds(colorIds:MutableList<String>): MutableList<VivoProducts>{
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("colorIds",colorIds);
        return namedParameterJdbcTemplate.query("""
                select *  from vivo_products t where t.color_id in(:colorIds)
        """,  paramMap,BeanPropertyRowMapper(VivoProducts::class.java));
    }

}

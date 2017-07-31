package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

interface VivoProductsRepository:BaseRepository<VivoProducts,String>,VivoProductsRepositoryCustom{

}

interface VivoProductsRepositoryCustom{
    fun findProducts():MutableList<VivoProducts>
    fun findColorIds(colorIdList: MutableList<String>):MutableList<String>
}

class VivoProductsRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):VivoProductsRepositoryCustom{

    override fun findProducts(): MutableList<VivoProducts> {
        val sb = "SELECT * FROM VR_Products"
        return namedParameterJdbcTemplate.query(sb,BeanPropertyRowMapper(VivoProducts::class.java))
    }

    override fun findColorIds(colorIdList: MutableList<String>): MutableList<String> {
        val map = Maps.newHashMap<String,Any>()
        map.put("colorIdList",colorIdList)
        val sb = "select t.colorId from vivo_products t where t.color_id in (:colorIdList)"
        return namedParameterJdbcTemplate.query(sb,map,BeanPropertyRowMapper(String::class.java))
    }
}


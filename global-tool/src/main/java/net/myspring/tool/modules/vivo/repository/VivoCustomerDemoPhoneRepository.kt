package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemLendM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class VivoCustomerDemoPhoneRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findDemoPhones(dateStart:String,dateEnd:String):MutableList<SProductItemLendM13e00>{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            select
                demo.shop_id as companyID,
                demo.created_date as updateTime,
                im.product_id as productID,
                im.ime as productNo
            from
                crm_demo_phone demo,
                crm_product_ime im
            where
                demo.product_ime_id = im.id
                and demo.created_date >=:dateStart
                and demo.created_date <:dateEnd
                and demo.enabled = 1
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(SProductItemLendM13e00::class.java))
    }
}
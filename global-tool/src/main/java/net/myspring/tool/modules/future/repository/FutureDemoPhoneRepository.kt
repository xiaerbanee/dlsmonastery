package net.myspring.tool.modules.future.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.oppo.domain.OppoCustomerDemoPhone
import net.myspring.tool.modules.vivo.domain.SProductItemLend
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FutureDemoPhoneRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(dateStart: String, dateEnd: String): MutableList<OppoCustomerDemoPhone> {
        val paramMap = Maps.newHashMap<String, Any>()
        paramMap.put("dateStart",dateStart)
        paramMap.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.query("""
             select
                    demo.shop_id as customerid,
                    demo.created_date as date,
                    im.product_id as productCode,
                    im.ime as imei
                from
                    crm_demo_phone demo,
                    crm_product_ime im
                where
                    demo.product_ime_id = im.id
                    and demo.created_date >=:dateStart
                    and demo.created_date <:dateEnd
                    and demo.enabled = 1
                """,paramMap, BeanPropertyRowMapper(OppoCustomerDemoPhone::class.java))
    }
    fun findDemoPhones(dateStart:String,dateEnd:String):MutableList<SProductItemLend>{
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
        return namedParameterJdbcTemplate.query(sb.toString(),map, BeanPropertyRowMapper(SProductItemLend::class.java))
    }
}
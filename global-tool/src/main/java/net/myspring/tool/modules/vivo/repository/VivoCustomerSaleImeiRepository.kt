package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.dto.VivoCustomerSaleImeiDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class VivoCustomerSaleImeiRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findProductSaleImei (dateStart:String,dateEnd:String):MutableList<VivoCustomerSaleImeiDto>{
        val map = Maps.newHashMap<String,String>();
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
             select
                    im.ime as imei,
                    im.product_id as productId,
                    sa.shop_id as shopId,
                    sa.created_date as saleTime,
                    sa.buyer as customerName,
                    sa.buyer_phone as customerMobile,
                    sa.buyer_sex as customerSex,
                    sa.employee_id as salePromoter,
                    de.id as shopCode,
                    de.name as shopName,
                    de.district_id as province
            from
                    crm_product_ime_sale sa,
                    crm_product_ime im,
                    crm_depot de
            where
                    sa.created_date >=:dateStart
                    and sa.created_date < :dateEnd
                    and sa.product_ime_id = im.id
                    and sa.shop_id = de.id
                    and sa.is_back = 0
                    and sa.enabled = 1
             order by sa.shop_id asc
        """)

        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(VivoCustomerSaleImeiDto::class.java))
    }
}
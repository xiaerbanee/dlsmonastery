package net.myspring.tool.modules.future.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleCount
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleImei
import net.myspring.tool.modules.vivo.dto.VivoCustomerSaleImeiDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FutureProductImeSaleRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findCustomerSaleCounts(dateStart: String, dateEnd: String): MutableList<OppoCustomerSaleCount>{
        val paramMap = Maps.newHashMap<String, Any>()
        paramMap.put("dateStart",dateStart)
        paramMap.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.query("""
             select
                sa.shop_id as shopCode,
                date_format(sa.created_date,'%Y-%m-%d') as saleTime,
                im.product_id as productCode,
                count(*) as qty
            from
                crm_product_ime_sale sa,crm_product_ime im
            where
                    sa.product_ime_id=im.id
                    and sa.created_date >=:dateStart
                    and sa.created_date <:dateEnd
                    and sa.is_back = 0
                    and sa.enabled = 1
                    and im.enabled=1
            group by
                    shopCode,saleTime,productCode
                 """, paramMap, BeanPropertyRowMapper(OppoCustomerSaleCount::class.java))
    }

    fun findCustomerSaleImeis(dateStart: String, dateEnd: String): MutableList<OppoCustomerSaleImei> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);

        return namedParameterJdbcTemplate.query("""
             select
                    im.ime as imei,
                    sa.created_date as saletime,
                    sa.buyer as custname,
                    sa.buyer_phone as custmobile,
                    sa.buyer_sex as custsex,
                    sa.employee_id as salepromoter ,
                    de.id as shopcode,
                    de.name as shopname,
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
                """, paramMap, BeanPropertyRowMapper(OppoCustomerSaleImei::class.java));
    }

    fun findCustomerSales(dateStart: String, dateEnd: String): MutableList<OppoCustomerSale> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);

        return namedParameterJdbcTemplate.query("""
             select
                    sa.shop_id as customerId,
                    date_format(sa.created_date,'%Y-%m-%d') as date,
                    count(*) as totalSaleQty
                from
                    crm_product_ime_sale sa
                where
                    sa.created_date >=:dateStart
                    and sa.created_date <:dateEnd
                    and sa.is_back = 0
                    and sa.enabled = 1
                group by
                    sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')
                order by sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')  asc
             """, paramMap, BeanPropertyRowMapper(OppoCustomerSale::class.java));
    }

    fun findProductSaleImei (dateStart:String,dateEnd:String):MutableList<VivoCustomerSaleImeiDto>{
        val map = Maps.newHashMap<String,String>();
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
             select
                    de.province_id as provinceId,
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
                    and de.province_id is not null
                    and sa.is_back = 0
                    and sa.enabled = 1
             order by sa.shop_id asc
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(VivoCustomerSaleImeiDto::class.java))
    }

}
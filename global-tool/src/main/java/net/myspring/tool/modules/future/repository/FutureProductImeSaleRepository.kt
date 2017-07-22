package net.myspring.tool.modules.future.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleCount
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleImei
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
}
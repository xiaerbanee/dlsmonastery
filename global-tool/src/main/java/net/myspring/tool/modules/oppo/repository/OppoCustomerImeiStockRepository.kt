package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerDemoPhone
import net.myspring.tool.modules.oppo.domain.OppoCustomerImeiStock
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerImeiStockRepository : BaseRepository<OppoCustomerImeiStock, String>, OppoCustomerImeiStockRepositoryCustom {

}
interface OppoCustomerImeiStockRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String): MutableList<OppoCustomerImeiStock>
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerImeiStock>

}
class OppoCustomerImeiStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerImeiStockRepositoryCustom{
    override fun findAll(dateStart: String, dateEnd: String): MutableList<OppoCustomerImeiStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);

        return namedParameterJdbcTemplate.query("""
         select
                go.shop_id as customerid,im.ime as imei,goi.product_id as productcode,0 as transType,go.ship_date as date
            from
                crm_goods_order_ime goi,
                crm_goods_order go,
                crm_product_ime im
            where
                goi.goods_order_id = go.id
                and goi.product_ime_id=im.id
                and go.ship_date>=:dateStart
                and go.ship_date<:dateEnd
                and go.enabled=1
                and go.shop_id in (select depot_id from crm_depot_shop where depot_id not in (select depot_id from crm_depot_store))
            union
            select
                al.from_depot_id as customerid,im.ime as imei,pro.id as productcode,1 as transType,al.created_date as date
            from
                crm_ime_allot al,
                crm_product pro,
                crm_product_ime im
            where
                al.product_ime_id = im.id
                and im.product_id = pro.id
                and al.created_date>=:dateStart
                and al.created_date<:dateEnd
                and al.enabled=1
                and al.from_depot_id in (select depot_id from crm_depot_shop where depot_id not in (select depot_id from crm_depot_store))
            union
            select
                al.to_depot_id as customerid,im.ime as imei,pro.id as productcode,0 as transType,al.created_date as date
            from
                crm_ime_allot al,
                crm_product pro,
                crm_product_ime im
            where
                al.product_ime_id = im.id
                and im.product_id = pro.id
                and al.created_date>=:dateStart
                and al.created_date<:dateEnd
                and al.enabled=1
                and al.to_depot_id in (select depot_id from crm_depot_shop where depot_id not in (select depot_id from crm_depot_store))
            union
            select
                af.from_depot_id as customerid,im.ime as imei, pro.id as productcode,1 as transType,af.created_date as date
            from
                crm_after_sale_ime_allot af,
                crm_product_ime im,
                crm_product pro
            where
                af.product_ime_id=im.id
                and im.product_id=pro.id
                and af.created_date>=:dateStart
                and af.created_date<:dateEnd
                and af.enabled=1
                and af.from_depot_id in (select depot_id from crm_depot_shop where depot_id not in (select depot_id from crm_depot_store))
            union
            select
                af.to_depot_id as customerid,im.ime as imei, pro.id as productcode,0 as transType,af.created_date as date
            from
                crm_after_sale_ime_allot af,
                crm_product_ime im,
                crm_product pro
            where
                af.product_ime_id=im.id
                and im.product_id=pro.id
                and af.created_date>=:dateStart
                and af.created_date<:dateEnd
                and af.enabled=1
                and af.to_depot_id in (select depot_id from crm_depot_shop where depot_id not in (select depot_id from crm_depot_store))
            """,paramMap, BeanPropertyRowMapper(OppoCustomerImeiStock::class.java));
    }

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerImeiStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_imei_stock where date >=:dateStart and date <:dateEnd
         """,paramMap, BeanPropertyRowMapper(OppoCustomerImeiStock::class.java));
    }
}

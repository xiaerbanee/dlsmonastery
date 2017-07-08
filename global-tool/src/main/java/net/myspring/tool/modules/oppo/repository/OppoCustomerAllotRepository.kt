package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerAfterSaleImei
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerAllotRepository : BaseRepository<OppoCustomerAllot, String>, OppoCustomerAllotRepositoryCustom {

}
interface OppoCustomerAllotRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String): MutableList<OppoCustomerAllot>
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerAllot>

}
class OppoCustomerAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerAllotRepositoryCustom{
    override fun findAll(dateStart: String,dateEnd: String) : MutableList<OppoCustomerAllot>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);

        return namedParameterJdbcTemplate.query("""
            select
                go.store_id as fromCustomerid,go.shop_id as toCustomerid,de.product_id as productcode,go.ship_date as date,sum(de.shipped_qty) as qty
                from
                    crm_goods_order go,
                    crm_goods_order_detail de
                where
                    go.id = de.goods_order_id
                    and go.ship_date>=:dateStart
                    and go.ship_date<=:dateEnd
                    and de.shipped_qty >0
                    and go.enabled=1
                    group by go.store_id,go.shop_id,de.product_id
            union
            select
                st.from_store_id as fromCustomerid,st.to_store_id as toCustomerid,de.product_id as productcode,st.ship_date as date,sum(de.shipped_qty) as qty
                from
                    crm_store_allot st,
                    crm_store_allot_detail de
                where
                    de.store_allot_id=st.id
                    and st.ship_date>=:dateStart
                    and st.ship_date<=:dateEnd
                    and de.shipped_qty>0
                    and st.enabled=1
                    group by st.from_store_id,st.to_store_id,de.product_id
            union
            select
                t.from_depot_id as fromCustomerid,t.to_depot_id as toCustomerid,im.product_id as productcode,t.audit_date as date,count(*) as qty
                from
                    crm_ime_allot t,crm_product_ime im
                where
                    t.audit_date >= :dateStart
                    and t.audit_date <= :dateEnd
                    and t.enabled = 1
                    and t.`status` = '已通过'
                    and t.product_ime_id=im.id
                    group by t.from_depot_id,t.to_depot_id,im.product_id
        """, paramMap, BeanPropertyRowMapper(OppoCustomerAllot::class.java));
    }

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerAllot> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_allot where date >=:dateStart and date <:dateEnd
         """,paramMap, BeanPropertyRowMapper(OppoCustomerAllot::class.java));
    }
}

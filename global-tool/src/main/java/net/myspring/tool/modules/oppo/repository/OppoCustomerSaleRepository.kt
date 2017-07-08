package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleImei
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleRepository : BaseRepository<OppoCustomerSale, String>, OppoCustomerSaleRepositoryCustom {

}
interface OppoCustomerSaleRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String): MutableList<OppoCustomerSale>
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerSale>

}
class OppoCustomerSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleRepositoryCustom{
    override fun findAll(dateStart: String, dateEnd: String): MutableList<OppoCustomerSale> {
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
                    and sa.created_date <=:dateEnd
                    and sa.is_back = 0
                    and sa.enabled = 1
                group by
                    sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')
                order by sa.shop_id,date_format(sa.created_date,'%Y-%m-%d')  asc
             """, paramMap, BeanPropertyRowMapper(OppoCustomerSale::class.java));
    }

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerSale> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_sale where date >=:dateStart and date <:dateEnd
         """,paramMap,BeanPropertyRowMapper(OppoCustomerSale::class.java));
    }
}

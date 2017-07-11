package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot
import net.myspring.tool.modules.oppo.domain.OppoCustomerDemoPhone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerDemoPhoneRepository : BaseRepository<OppoCustomerDemoPhone, String>, OppoCustomerDemoPhoneRepositoryCustom {

}
interface OppoCustomerDemoPhoneRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String): MutableList<OppoCustomerDemoPhone>
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerDemoPhone>

}
class OppoCustomerDemoPhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerDemoPhoneRepositoryCustom{
    override fun findAll(dateStart: String, dateEnd: String): MutableList<OppoCustomerDemoPhone> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
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
                """,paramMap, BeanPropertyRowMapper(OppoCustomerDemoPhone::class.java));
    }

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerDemoPhone> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_demo_phone where date >=:dateStart and date <:dateEnd
         """,paramMap, BeanPropertyRowMapper(OppoCustomerDemoPhone::class.java));
    }
}

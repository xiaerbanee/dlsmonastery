package net.myspring.future.modules.third.repository;

import com.google.common.collect.Maps
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.third.domain.OppoCustomerDemoPhone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerDemoPhoneRepository : BaseRepository<OppoCustomerDemoPhone, String>,OppoCustomerDemoPhoneRepositoryCustom {

}

interface OppoCustomerDemoPhoneRepositoryCustom{
    fun findAll(dateStart: String,dateEnd: String,companyId:String): MutableList<OppoCustomerDemoPhone>

}
class OppoCustomerDemoPhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerDemoPhoneRepositoryCustom {
    override fun findAll(dateStart: String, dateEnd: String, companyId:String): MutableList<OppoCustomerDemoPhone> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("companyId",companyId);
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
                    and demo.created_date <=:dateEnd
                    and demo.enabled = 1
                    and demo.company_id=:companyId
                """,paramMap, BeanPropertyRowMapper(OppoCustomerDemoPhone::class.java));

    }
}
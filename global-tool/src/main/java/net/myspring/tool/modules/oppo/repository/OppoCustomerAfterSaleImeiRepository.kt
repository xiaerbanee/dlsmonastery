package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerAfterSaleImei
import net.myspring.tool.modules.oppo.domain.OppoCustomerOperatortype
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerAfterSaleImeiRepository : BaseRepository<OppoCustomerAfterSaleImei, String>, OppoCustomerAfterSaleImeiRepositoryCustom {

}
interface OppoCustomerAfterSaleImeiRepositoryCustom{
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerAfterSaleImei>
    fun deleteByCompanyNameAndDate(companyName:String,dateStart: String,dateEnd: String):Int
}
class OppoCustomerAfterSaleImeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerAfterSaleImeiRepositoryCustom{

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerAfterSaleImei> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_after_sale_imei where date >=:dateStart and date <:dateEnd
         """,paramMap, BeanPropertyRowMapper(OppoCustomerAfterSaleImei::class.java));
    }

    override fun deleteByCompanyNameAndDate(companyName:String,dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("companyName",companyName)
        return namedParameterJdbcTemplate.update("""
          delete from oppo_push_customer_after_sale_imei
          where date >=:dateStart
            and date < :dateEnd
            and company_name = :companyName
        """,map)
    }
}

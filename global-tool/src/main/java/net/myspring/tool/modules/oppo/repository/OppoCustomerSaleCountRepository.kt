package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleCountRepository : BaseRepository<OppoCustomerSaleCount, String>, OppoCustomerSaleCountRepositoryCustom {

}
interface OppoCustomerSaleCountRepositoryCustom{
    fun findByDate(companyName:String,dateStart:String,dateEnd:String):MutableList<OppoCustomerSaleCount>
    fun deleteByCompanyNameAndDate(companyName:String,dateStart: String,dateEnd: String):Int
}
class OppoCustomerSaleCountRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleCountRepositoryCustom{

    override fun findByDate(companyName:String,dateStart:String, dateEnd:String): MutableList<OppoCustomerSaleCount> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("companyName",companyName);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_sale_count
            where sale_time >=:dateStart
              and sale_time < :dateEnd
              and company_name = :companyName
         """,paramMap,BeanPropertyRowMapper(OppoCustomerSaleCount::class.java));
    }

    override fun deleteByCompanyNameAndDate(companyName:String,dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("companyName",companyName)
        return namedParameterJdbcTemplate.update("""
            delete from oppo_push_customer_sale_count
            where sale_time >=:dateStart
              and sale_time < :dateEnd
              and company_name = :companyName
        """,map)
    }
}

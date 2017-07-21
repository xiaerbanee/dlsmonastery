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
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerSaleCount>
    fun deleteByDate(dateStart: String,dateEnd: String):Int
}
class OppoCustomerSaleCountRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleCountRepositoryCustom{

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerSaleCount> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_sale_count where sale_time >=:dateStart and sale_time <:dateEnd
         """,paramMap,BeanPropertyRowMapper(OppoCustomerSaleCount::class.java));
    }

    override fun deleteByDate(dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.update("delete from oppo_push_customer_sale_count where sale_time >=:dateStart and sale_time <:dateEnd",map)
    }
}

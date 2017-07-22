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
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerAllot>
    fun deleteByDate(dateStart: String,dateEnd: String):Int

}
class OppoCustomerAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerAllotRepositoryCustom{

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerAllot> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_allot where date >=:dateStart and date <:dateEnd
         """,paramMap, BeanPropertyRowMapper(OppoCustomerAllot::class.java));
    }

    override fun deleteByDate(dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.update("delete from oppo_push_customer_allot where date >=:dateStart and date <:dateEnd",map)
    }
}

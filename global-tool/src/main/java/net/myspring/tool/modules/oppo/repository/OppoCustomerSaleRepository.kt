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
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomerSale>
    fun deleteByDate(dateStart: String,dateEnd: String):Int
}
class OppoCustomerSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleRepositoryCustom{

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomerSale> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_sale where date >=:dateStart and date <:dateEnd
         """,paramMap,BeanPropertyRowMapper(OppoCustomerSale::class.java));
    }

    override fun deleteByDate(dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.update("delete from oppo_push_customer_sale where date >=:dateStart and date <:dateEnd",map)
    }
}

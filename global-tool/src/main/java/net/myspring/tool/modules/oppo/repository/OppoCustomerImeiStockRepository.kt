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
    fun findByDate(companyName:String,dateStart:String,dateEnd:String):MutableList<OppoCustomerImeiStock>
    fun deleteByCompanyNameAndDate(companyName:String,dateStart: String,dateEnd: String):Int
}
class OppoCustomerImeiStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerImeiStockRepositoryCustom{

    override fun findByDate(companyName:String,dateStart:String, dateEnd:String): MutableList<OppoCustomerImeiStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("companyName",companyName);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_imei_stock
            where date >=:dateStart
              and date < :dateEnd
              and company_name = :companyName
         """,paramMap, BeanPropertyRowMapper(OppoCustomerImeiStock::class.java));
    }

    override fun deleteByCompanyNameAndDate(companyName:String,dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("companyName",companyName)
        return namedParameterJdbcTemplate.update("""
          delete from oppo_push_customer_imei_stock
          where date >=:dateStart
            and date < :dateEnd
            and company_name = :companyName
        """,map)
    }
}

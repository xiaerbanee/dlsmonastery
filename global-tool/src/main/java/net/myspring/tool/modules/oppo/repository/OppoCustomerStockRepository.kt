package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerStockRepository : BaseRepository<OppoCustomerStock, String>, OppoCustomerStockRepositoryCustom {

}
interface OppoCustomerStockRepositoryCustom{
    fun findByDate(companyName:String,dateStart:String,dateEnd:String):MutableList<OppoCustomerStock>
    fun deleteByCompanyNameAndDate(companyName:String,dateStart: String,dateEnd: String):Int
}
class OppoCustomerStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerStockRepositoryCustom{

    override fun findByDate(companyName:String,dateStart:String, dateEnd:String): MutableList<OppoCustomerStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("companyName",companyName);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer_stock
            where date >=:dateStart
              and date < :dateEnd
              and company_name = :companyName
         """,paramMap,BeanPropertyRowMapper(OppoCustomerStock::class.java));
    }

    override fun deleteByCompanyNameAndDate(companyName:String,dateStart: String, dateEnd: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("companyName",companyName)
        return namedParameterJdbcTemplate.update("""
          delete from oppo_push_customer_stock
          where date >=:dateStart
            and date < :dateEnd
            and company_name = :companyName
        """,map)
    }
}

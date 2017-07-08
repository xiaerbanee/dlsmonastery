package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleImei
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerRepository : BaseRepository<OppoCustomer, String>, OppoCustomerRepositoryCustom {
}
interface OppoCustomerRepositoryCustom{
    fun findByDate(dateStart:String,dateEnd:String):MutableList<OppoCustomer>
}
class OppoCustomerRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerRepositoryCustom{

    override fun findByDate(dateStart:String, dateEnd:String): MutableList<OppoCustomer> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
            select *  from oppo_push_customer where created_date >=:dateStart and created_date<:dateEnd
         """,paramMap,BeanPropertyRowMapper(OppoCustomer::class.java));
    }
}

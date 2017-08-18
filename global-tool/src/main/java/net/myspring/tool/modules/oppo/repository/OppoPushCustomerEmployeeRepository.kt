package net.myspring.tool.modules.oppo.repository

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPushCustomerEmployee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoPushCustomerEmployeeRepository :BaseRepository<OppoPushCustomerEmployee,String>,OppoPushCustomerEmployeeRepositoryCustom{
    @Query("select t1 from  #{#entityName} t1 where t1.companyName = ?1")
    fun findByCompanyName(companyName:String):MutableList<OppoPushCustomerEmployee>

}

interface OppoPushCustomerEmployeeRepositoryCustom{
    fun deleteByCompanyName(companyName:String):Int
}

class OppoPushCustomerEmployeeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):OppoPushCustomerEmployeeRepositoryCustom{
    override fun deleteByCompanyName(companyName: String): Int {
        val map = Maps.newHashMap<String,String>()
        map.put("companyName",companyName)
        val sb = "delete from oppo_push_customer_employee where company_name = :companyName"
        return namedParameterJdbcTemplate.update(sb,map)
    }
}
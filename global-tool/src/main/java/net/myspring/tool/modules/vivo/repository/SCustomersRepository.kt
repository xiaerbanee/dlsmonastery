package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SCustomers
import net.myspring.tool.modules.vivo.dto.SCustomerDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SCustomersRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("DELETE FROM vivo_push_customers WHERE 1=1",map)
    }


    fun batchSave(sCustomersM13e00List: MutableList<SCustomers>): IntArray? {
        val sb = StringBuffer()
        sb.append("""
           insert into vivo_push_customers
            (
                CustomerID,
                CustomerName,
                ZoneID,
                CompanyID,
                RecordDate,
                CustomerLevel,
                Customerstr1,
                Customerstr4,
                Customerstr10,
                AgentCode
            )
            values(
                :customerId,
                :customerName,
                :zoneId,
                :companyId,
                :recordDate,
                :customerLevel,
                :customerStr1,
                :customerStr4,
                :customerStr10,
                :agentCode
            )""")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sCustomersM13e00List.toTypedArray()))
    }

    fun batchIDvivoSave(sCustomersM13e00List: MutableList<SCustomers>, agentCode:String): IntArray? {
        val sb = StringBuffer()
        sb.append("insert into ")
        sb.append(" S_Customers_"+agentCode)
        sb.append("""
            (
                CustomerID,
                CustomerName,
                ZoneID,
                CompanyID,
                RecordDate,
                CustomerLevel,
                Customerstr1,
                Customerstr4,
                Customerstr10
            )
            values(
                :customerId,
                :customerName,
                :zoneId,
                :companyId,
                :recordDate,
                :customerLevel,
                :customerStr1,
                :customerStr4,
                :customerStr10
            )""")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sCustomersM13e00List.toTypedArray()))
    }
}
package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemStocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SProductItemStocksRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteAll():Int{
        val map = Maps.newHashMap<String,String>()
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM vivo_push_productitemstocks
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun deleteByAgentCode(agentCode:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("agentCode",agentCode)
        val sb = " DELETE FROM S_ProductItemStocks_"+agentCode
        return namedParameterJdbcTemplate.update(sb,map)
    }

    fun batchSave(sProductItemStocksM13e00List:MutableList<SProductItemStocks>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO vivo_push_productitemstocks(CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark,UpdateTime,AgentCode)
            VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark,:updateTime,:agentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemStocksM13e00List.toTypedArray()))
    }

    fun batchSaveToFactory(agentCode: String,sProductItemStocksM13e00List:MutableList<SProductItemStocks>):IntArray{
        val sb = StringBuilder()
        sb.append(" INSERT INTO S_ProductItemStocks_")
        sb.append(agentCode+" ")
        sb.append(" (CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark) ")
        sb.append(" VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemStocksM13e00List.toTypedArray()))
    }

    fun findByAgentCodeIn(agentCodeList: MutableList<String>):MutableList<SProductItemStocks>{
        val map = Maps.newHashMap<String,Any>()
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_productitemstocks where AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SProductItemStocks::class.java))
    }

}
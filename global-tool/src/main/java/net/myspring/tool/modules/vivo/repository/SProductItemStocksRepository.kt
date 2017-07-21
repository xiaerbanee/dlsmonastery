package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemStocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SProductItemStocksRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteByUpdateTime(dateStart : String,dateEnd : String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM S_ProductItemStocks_M13E00
            WHERE UpdateTime >=  :dateStart and UpdateTime < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun deleteIDvivoByUpdateTime(dateStart : String,dateEnd : String,agentCode:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCode",agentCode)
        val sb = StringBuilder()
        sb.append(" DELETE FROM S_ProductItemStocks_")
        sb.append(agentCode+" ")
        sb.append(" WHERE UpdateTime >=  :dateStart and UpdateTime < :dateEnd ")
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sProductItemStocksM13e00List:MutableList<SProductItemStocks>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_ProductItemStocks_M13E00(CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark,UpdateTime)
            VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark,:updateTime)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemStocksM13e00List.toTypedArray()))
    }

    fun batchIDvivoSave(sProductItemStocksM13e00List:MutableList<SProductItemStocks>, agentCode: String):IntArray{
        val sb = StringBuilder()
        sb.append(" INSERT INTO S_ProductItemStocks_")
        sb.append(agentCode+" ")
        sb.append(" (CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark) ")
        sb.append(" VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemStocksM13e00List.toTypedArray()))
    }
}
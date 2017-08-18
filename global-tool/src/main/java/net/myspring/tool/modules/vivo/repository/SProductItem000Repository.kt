package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItem000
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SProductItem000Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteAll():Int{
        val map = Maps.newHashMap<String,String>()
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM vivo_push_productitem000
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun deleteByAgentCode(agentCode:String):Int{
        val map = Maps.newHashMap<String,String>()
        val sb = "DELETE FROM S_ProductItem000_"+agentCode
        return namedParameterJdbcTemplate.update(sb,map)
    }

    fun batchSave(sProductItem000M13e00List:MutableList<SProductItem000>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO vivo_push_productitem000(CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark,UpdateTime,AgentCode)
            VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark,:updateTime,:agentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItem000M13e00List.toTypedArray()))
    }

    fun batchSaveToFactory(agentCode: String,sProductItem000M13e00List:MutableList<SProductItem000>):IntArray{
        val sb = StringBuilder()
        sb.append("INSERT INTO S_ProductItem000_")
        sb.append(agentCode+" ")
        sb.append(" (CompanyID,ProductID,ProductNo,StoreID,CustomerID,SubCustomerID,Status,StatusInfo,IsReturnProfit,IsLock,Remark)")
        sb.append(" VALUES (:companyId,:productId,:productNo,:storeId,:customerId,:subCustomerId,:status,:statusInfo,:isReturnProfit,:isLock,:remark)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItem000M13e00List.toTypedArray()))
    }

    fun findByIsUploadAndAgentCodeIn(agentCodeList: MutableList<String>):MutableList<SProductItem000>{
        val map = Maps.newHashMap<String,Any>()
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_productitem000 where IsUpload = 0 and AgentCode in (:agentCodeList) LIMIT 0,2000 "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SProductItem000::class.java))
    }

    fun updateIsUploadById(idList:MutableList<String>):Int{
        val map = Maps.newHashMap<String,Any>()
        map.put("idList",idList)
        return namedParameterJdbcTemplate.update("update vivo_push_productitem000 set IsUpload = 1 where id in (:idList)",map)
    }

}
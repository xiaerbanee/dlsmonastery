package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantStockSupply
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SPlantStockSupplyRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteByAccountDate(dateStart: String, dateEnd: String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder("""
            delete from vivo_push_plantstocksupply
            where AccountDate >= :dateStart
                and AccountDate < :dateEnd
        """);
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }


    fun deleteIDvivoByAccountDate(dateStart: String, dateEnd: String,agentCode:String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCode",agentCode)
        val sb = StringBuilder()
        sb.append(" delete from s_PlantStockSupply_")
        sb.append(agentCode+" ")
            sb.append(" where AccountDate >= :dateStart and AccountDate < :dateEnd ")
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }


    fun batchSave(sPlantStockSupplyM13e00List: MutableList<SPlantStockSupply>):IntArray?{
        val sb = StringBuilder()
        sb.append("""
            insert into vivo_push_plantstocksupply(CompanyID,SupplyID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate,AgentCode)
            values(:companyId,:supplyId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate,:agentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockSupplyM13e00List.toTypedArray()))
    }

    fun batchIDvivoSave(sPlantStockSupplyM13e00List: MutableList<SPlantStockSupply>, agentCode: String):IntArray?{
        val sb = StringBuilder()
        sb.append(" insert into s_PlantStockSupply_")
        sb.append(agentCode+" ")
        sb.append(" (CompanyID,SupplyID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate) ")
        sb.append(" values(:companyId,:supplyId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockSupplyM13e00List.toTypedArray()))
    }

}
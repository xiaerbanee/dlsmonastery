package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantStockDealer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SPlantStockDealerRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun deleteByAccountDate(dateStart: String, dateEnd: String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder("""
            delete from vivo_push_plantstockdealer
            where AccountDate >= :dateStart
                and AccountDate < :dateEnd
        """);
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun deleteByAccountDateAndAgentCode(dateStart: String, dateEnd: String,agentCode: String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("delete from S_PlantStockDealer_"+agentCode)
        sb.append(" where AccountDate >= :dateStart and AccountDate < :dateEnd")
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }


    fun batchSave(sPlantStockDealerM13e00List: MutableList<SPlantStockDealer>):IntArray?{
        val sb = StringBuilder()
        sb.append("""
            insert into vivo_push_plantstockdealer(CompanyID,DealerID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate,AgentCode)
            values(:companyId,:dealerId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate,:agentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockDealerM13e00List.toTypedArray()))
    }

    fun batchSaveToFactory(agentCode: String,sPlantStockDealerM13e00List: MutableList<SPlantStockDealer>):IntArray?{
        val sb = StringBuilder()
        sb.append("insert into s_PlantStockDealer_")
        sb.append(agentCode+" ")
        sb.append(" (CompanyID,DealerID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate) ")
        sb.append(" values(:companyId,:dealerId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockDealerM13e00List.toTypedArray()))
    }

    fun findByDateAndAgentCodeIn(dateStart: String,dateEnd: String,agentCodeList: MutableList<String>):MutableList<SPlantStockDealer>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_plantstockdealer where AccountDate >= :dateStart and AccountDate < :dateEnd and AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SPlantStockDealer::class.java))
    }


}
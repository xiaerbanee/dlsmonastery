package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SZones
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SZonesRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("DELETE FROM vivo_push_zones WHERE 1=1",map)
    }

    fun batchSave(sZonesM13e00List: MutableList<SZones>): IntArray {
        val sb = StringBuilder()
        sb.append("insert into vivo_push_zones (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes,agentCode)")
        sb.append("values (:zoneId,:zoneName,:shortcut,:zoneDepth,:zonePath,:fatherId,:subCount,:zoneTypes,:agentCode)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sZonesM13e00List.toTypedArray()))
    }

    fun batchSaveToFactroy(agentCode: String,sZonesList: MutableList<SZones>): IntArray {
        val sb = StringBuilder()
        sb.append(" insert into ")
        sb.append(" S_zones_"+agentCode)
        sb.append(" (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes) ")
        sb.append(" values (:zoneId,:zoneName,:shortcut,:zoneDepth,:zonePath,:fatherId,:subCount,:zoneTypes) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sZonesList.toTypedArray()))
    }

    fun deleteByAgentCode(agentCode:String){
        val map = Maps.newHashMap<String,String>()
        val sb = "delete from S_ZONES_"+agentCode
        namedParameterJdbcTemplate.update(sb, map)
    }

    fun findByAgentCodeIn(agentCodeList: MutableList<String>):MutableList<SZones>{
        var paramMap = Maps.newHashMap<String, Any>()
        paramMap.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_zones where AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, paramMap, BeanPropertyRowMapper(SZones::class.java))
    }
}

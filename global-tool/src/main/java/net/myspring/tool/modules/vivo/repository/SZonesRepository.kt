package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SZones
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SZonesRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("DELETE FROM S_ZONEs_M13E00 WHERE 1=1",map)
    }

    fun batchSave(sZonesM13e00List: MutableList<SZones>): IntArray {
        val sb = StringBuilder()
        sb.append("insert into S_ZONEs_M13E00 (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes)")
        sb.append("values (:zoneId,:zoneName,:shortcut,:zoneDepth,:zonePath,:fatherId,:subCount,:zoneTypes)")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sZonesM13e00List.toTypedArray()))
    }

    fun batchSaveIDvivo(agentCode: String,sZonesM13e00List: MutableList<SZones>): IntArray {
        val sb = StringBuilder()
        sb.append(" insert into ")
        sb.append(" S_zones_"+agentCode)
        sb.append(" (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes) ")
        sb.append(" values (:zoneId,:zoneName,:shortcut,:zoneDepth,:zonePath,:fatherId,:subCount,:zoneTypes) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sZonesM13e00List.toTypedArray()))
    }

    fun deleteIDvivoZones(){
        val map = Maps.newHashMap<String,String>()
        namedParameterJdbcTemplate.update("""
        delete from S_ZONES_R250082;
        delete from S_ZONES_R2500821;
        delete from S_ZONES_R2500822;
        delete from S_ZONES_R2500823;
        """, map)
    }
}

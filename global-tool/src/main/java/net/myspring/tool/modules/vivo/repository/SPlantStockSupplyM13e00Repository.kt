package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantStockSupplyM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SPlantStockSupplyM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteByAccountDate(dateStart: String, dateEnd: String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder("""
            delete from s_PlantStockSupply_m13e00
            where AccountDate >= :dateStart
                and AccountDate < :dateEnd
        """);
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sPlantStockSupplyM13e00List: MutableList<SPlantStockSupplyM13e00>):IntArray?{
        val sb = StringBuilder()
        sb.append("""
            insert into s_PlantStockSupply_m13e00(CompanyID,SupplyID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate)
            values(:companyId,:supplyId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockSupplyM13e00List.toTypedArray()))
    }

}
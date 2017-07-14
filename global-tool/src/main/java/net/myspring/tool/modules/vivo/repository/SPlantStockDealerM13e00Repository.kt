package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantStockDealerM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SPlantStockDealerM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun deleteByAccountDate(dateStart: String, dateEnd: String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder("""
            delete from s_PlantStockDealer_m13e00
            where AccountDate >= :dateStart
                and AccountDate < :dateEnd
        """);
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sPlantStockDealerM13e00List: MutableList<SPlantStockDealerM13e00>):IntArray?{
        val sb = StringBuilder()
        sb.append("""
            insert into s_PlantStockDealer_m13e00(CompanyID,DealerID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate)
            values(:companyid,:dealerid,:productid,:createdtime,:sumstock,:useablestock,:bad,:accountdate)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(), SqlParameterSourceUtils.createBatch(sPlantStockDealerM13e00List.toTypedArray()))
    }


}
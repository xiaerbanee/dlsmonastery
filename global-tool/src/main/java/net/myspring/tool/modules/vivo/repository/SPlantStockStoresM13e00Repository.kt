package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantStockStoresM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SPlantStockStoresM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun deleteByAccountDate(dateStart: String,dateEnd:String):Int {
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder("""
            delete from s_PlantStockStores_m13e00
            where AccountDate >= :dateStart
                and AccountDate < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sPlantStockStoreM13e00List: MutableList<SPlantStockStoresM13e00>):IntArray?{
        val sb = StringBuilder()
        sb.append("""
            insert into s_PlantStockStores_m13e00(CompanyID,StoreID,ProductID,CreatedTime,sumstock,useablestock,bad,AccountDate)
            values(:companyId,:storeId,:productId,:createdTime,:sumStock,:useAbleStock,:bad,:accountDate)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sPlantStockStoreM13e00List.toTypedArray()))
    }
}
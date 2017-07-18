package net.myspring.tool.modules.vivo.repository

import net.myspring.tool.modules.vivo.domain.SStoresM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SStoresM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun batchSave(sStoresM13e00List: MutableList<SStoresM13e00>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_Stores_M13E00(StoreID,StoreName,Remark,ShortCut)
                VALUES (:storeID,:storeName,:remark,:shortCut)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sStoresM13e00List.toTypedArray()))
    }
}
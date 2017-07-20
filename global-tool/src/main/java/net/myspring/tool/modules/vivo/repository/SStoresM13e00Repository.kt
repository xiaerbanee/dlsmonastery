package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SStoresM13e00
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SStoresM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("DELETE FROM S_Stores_M13E00",map)
    }

    fun batchSave(sStoresM13e00List: MutableList<SStoresM13e00>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_Stores_M13E00(StoreID,StoreName,Remark,ShortCut)
                VALUES (:storeID,:storeName,:remark,:shortCut)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sStoresM13e00List.toTypedArray()))
    }

    fun deleteIDvivoStores(agentCode:String):Int{
        val map = Maps.newHashMap<String,Any>()
        map.put("agentCode",agentCode)
        val sb = StringBuilder()
        sb.append("delete from S_Stores_"+agentCode)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }


    fun findIDvivoStore():MutableList<SStoresM13e00>{
        val sb = StringBuilder()
        sb.append("""
          select
             de.province_id as storeID,'' as storeName,'' as agentCode,store.joint_level as jointLevel
        from
            crm_depot de,
            crm_depot_store store
        where
            de.id=store.depot_id
            and (store.joint_level='一级' or store.joint_level='二级')
            group by de.province_id
            order by store.joint_level asc
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertyRowMapper(SStoresM13e00::class.java))
    }

    fun batchIDvivoSave(sStoresM13e00List: MutableList<SStoresM13e00>,agentCode: String):IntArray{
        val sb = StringBuilder()
        sb.append("INSERT INTO S_Stores_"+agentCode)
        sb.append(" (StoreID,StoreName,Remark,ShortCut) ")
        sb.append(" VALUES (:storeID,:storeName,:remark,:shortCut) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sStoresM13e00List.toTypedArray()))
    }


}
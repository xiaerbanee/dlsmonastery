package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SStores
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SStoresRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteAll():Int{
        val map = Maps.newHashMap<String,Any>()
        return namedParameterJdbcTemplate.update("DELETE FROM vivo_push_stores",map)
    }

    fun batchSave(sStoresM13e00List: MutableList<SStores>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO vivo_push_stores(StoreID,StoreName,Remark,ShortCut,AgentCode)
                VALUES (:storeID,:storeName,:remark,:shortCut,:AgentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sStoresM13e00List.toTypedArray()))
    }

    fun deleteByAgentCode(agentCode:String):Int{
        val map = Maps.newHashMap<String,Any>()
        map.put("agentCode",agentCode)
        val sb = StringBuilder()
        sb.append("delete from S_Stores_"+agentCode)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }


    fun findIDvivoStore():MutableList<SStores>{
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
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertyRowMapper(SStores::class.java))
    }

    fun batchSaveToFactory(agentCode: String,sStoresM13e00List: MutableList<SStores>):IntArray{
        val sb = StringBuilder()
        sb.append("INSERT INTO S_Stores_"+agentCode)
        sb.append(" (StoreID,StoreName,Remark,ShortCut) ")
        sb.append(" VALUES (:storeID,:storeName,:remark,:shortCut) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sStoresM13e00List.toTypedArray()))
    }

    fun findByAgentCodeIn(agentCodeList: MutableList<String>):MutableList<SStores>{
        val map = Maps.newHashMap<String,Any>()
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_stores where AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SStores::class.java))
    }

}
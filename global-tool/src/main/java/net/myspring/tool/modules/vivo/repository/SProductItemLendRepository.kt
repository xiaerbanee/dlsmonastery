package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemLend
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SProductItemLendRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){


    fun deleteByUpdateTime(dateStart:String,dateEnd:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM vivo_push_productitemlend WHERE UpdateTime >= :dateStart AND UpdateTime < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sProductItemLendM13e00List: MutableList<SProductItemLend>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO vivo_push_productitemlend (CompanyID,ProductID,ProductNo,StoreID,Status,StatusInfo,UpdateTime)
            VALUES (:companyID,:productID,:productNo,:storeID,:status,:statusInfo,:updateTime)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemLendM13e00List.toTypedArray()))
    }

    fun findByDateAndAgentCodeIn(dateStart: String,dateEnd: String,agentCodeList: MutableList<String>):MutableList<SProductItemLend>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from S_ProductItemLend_M13E00 where UpdateTime >= :dateStart and UpdateTime <dateEnd and AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SProductItemLend::class.java))
    }


}
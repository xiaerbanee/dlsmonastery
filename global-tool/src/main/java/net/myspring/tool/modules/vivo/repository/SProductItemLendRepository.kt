package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemLend
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
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
            DELETE FROM S_ProductItemLend_M13E00 WHERE UpdateTime >= :dateStart AND UpdateTime < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sProductItemLendM13e00List: MutableList<SProductItemLend>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_ProductItemLend_M13E00 (CompanyID,ProductID,ProductNo,StoreID,Status,StatusInfo,UpdateTime)
            VALUES (:companyID,:productID,:productNo,:storeID,:status,:statusInfo,:updateTime)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemLendM13e00List.toTypedArray()))
    }
}
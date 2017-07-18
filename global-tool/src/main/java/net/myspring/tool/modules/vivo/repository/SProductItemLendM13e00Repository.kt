package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SProductItemLendM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SProductItemLendM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findDemoPhones(dateStart:String,dateEnd:String):MutableList<SProductItemLendM13e00>{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            select
                demo.shop_id as companyID,
                demo.created_date as updateTime,
                im.product_id as productID,
                im.ime as productNo
            from
                crm_demo_phone demo,
                crm_product_ime im
            where
                demo.product_ime_id = im.id
                and demo.created_date >=:dateStart
                and demo.created_date <:dateEnd
                and demo.enabled = 1
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),map, BeanPropertyRowMapper(SProductItemLendM13e00::class.java))
    }

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

    fun batchSave(sProductItemLendM13e00List: MutableList<SProductItemLendM13e00>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_ProductItemLend_M13E00 (CompanyID,ProductID,ProductNo,StoreID,Status,StatusInfo,UpdateTime)
            VALUES (:companyID,:productID,:productNo,:storeID,:status,:statusInfo,:updateTime)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sProductItemLendM13e00List.toTypedArray()))
    }
}
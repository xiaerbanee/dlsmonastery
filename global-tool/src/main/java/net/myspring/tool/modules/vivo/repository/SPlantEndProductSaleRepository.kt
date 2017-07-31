package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantEndProductSale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SPlantEndProductSaleRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteByBillDate(dateStart:String,dateEnd:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM vivo_push_plantendproductsale WHERE BillDate > :dateStart
                AND BillDate < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun deleteByBillDateAndAgentCode(dateStart:String,dateEnd:String,agentCode:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append(" delete from S_PlantEndProductSale_"+agentCode)
        sb.append(" where BillDate > :dateStart and BillDate < :dateEnd")
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sPlantEndProduuctSaleImeiList: MutableList<SPlantEndProductSale>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO vivo_push_plantendproductsale(CompanyID,EndBillID,ProductID,SaleCount,Imei,BillDate,DealerID,SalePrice,CreatedTime,AgentCode)
            VALUES (:companyID,:endBillID,:productID,:saleCount,:imei,:billDate,:dealerID,:salePrice,:createdTime,:agentCode)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sPlantEndProduuctSaleImeiList.toTypedArray()))
    }

    fun batchSaveToFactory(agentCode: String,sPlantEndProduuctSaleImeiList: MutableList<SPlantEndProductSale>):IntArray{
        val sb = StringBuilder()
        sb.append("INSERT S_PlantEndProductSale_"+agentCode)
        sb.append(" (CompanyID,EndBillID,ProductID,SaleCount,Imei,BillDate,DealerID,SalePrice,CreatedTime) ")
        sb.append(" VALUES (:companyID,:endBillID,:productID,:saleCount,:imei,:billDate,:dealerID,:salePrice,:createdTime) ")
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sPlantEndProduuctSaleImeiList.toTypedArray()))
    }

    fun findByDateAndAgentCodeIn(dateStart: String,dateEnd: String,agentCodeList: MutableList<String>):MutableList<SPlantEndProductSale>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        map.put("agentCodeList",agentCodeList)
        val sb = "select * from vivo_push_plantendproductsale where CreatedTime >= :dateStart and CreatedTime < :dateEnd and AgentCode in (:agentCodeList) "
        return namedParameterJdbcTemplate.query(sb, map, BeanPropertyRowMapper(SPlantEndProductSale::class.java))
    }



}
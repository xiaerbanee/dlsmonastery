package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.domain.SPlantEndProductSaleM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component

@Component
class SPlantEndProductSaleM13e00Repository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun deleteByBillDate(dateStart:String,dateEnd:String):Int{
        val map = Maps.newHashMap<String,String>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            DELETE FROM S_PLANTENDPRODUCTSALE_M13E00 WHERE BillDate > :dateStart
                AND BillDate < :dateEnd
        """)
        return namedParameterJdbcTemplate.update(sb.toString(),map)
    }

    fun batchSave(sPlantEndProduuctSaleImeiList: MutableList<SPlantEndProductSaleM13e00>):IntArray{
        val sb = StringBuilder()
        sb.append("""
            INSERT INTO S_PLANTENDPRODUCTSALE_M13E00(CompanyID,EndBillID,ProductID,SaleCount,Imei,BillDate,DealerID,SalePrice,CreatedTime)
            VALUES (:companyID,:endBillID,:productID,:saleCount,:imei,:billDate,:dealerID,:salePrice,:createdTime)
        """)
        return namedParameterJdbcTemplate.batchUpdate(sb.toString(),SqlParameterSourceUtils.createBatch(sPlantEndProduuctSaleImeiList.toTypedArray()))
    }

}
package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.StkInventory
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * 其他出库单
 * Created by lihx on 2017/6/16.
 */
@Component
class StkInventoryRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByStockIds(stockIds:List<String>): MutableList<StkInventory>?{
        return namedParameterJdbcTemplate.query("""
        SELECT
            t1.FSTOCKID,
            t1.FMATERIALID,
            t1.FBASEQTY
        FROM
            T_STK_INVENTORY t1
        WHERE
            t1.FSTOCKID in (:stockIds)
        """, Collections.singletonMap("stockIds",stockIds), BeanPropertyRowMapper(StkInventory::class.java))
    }

    fun findByStockIdAndMaterialId(stockId:String,materialId:String): StkInventory?{
        var sb = StringBuilder()
        sb.append("""
        SELECT
            t1.FSTOCKID,
            t1.FMATERIALID,
            t1.FBASEQTY
        FROM
            T_STK_INVENTORY t1
        WHERE
            t1.FSTOCKID in = :stockId
            t1.FMATERIALID = :materialId
        """)
        sb.append(" t1.FSTOCKID in = :stockId ")
        sb.append(" and t1.FMATERIALID = :materialId ")
        var paramMap = HashMap<String, Any>()
        paramMap.put("stockId", stockId)
        paramMap.put("materialId", materialId)
        return namedParameterJdbcTemplate.queryForObject(sb.toString(), paramMap, BeanPropertyRowMapper(StkInventory::class.java))
    }
}
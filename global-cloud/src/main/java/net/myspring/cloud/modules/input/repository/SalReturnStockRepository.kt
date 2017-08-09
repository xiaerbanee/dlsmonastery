package net.myspring.cloud.modules.input.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 销售退货单
 * Created by lihx on 2017/6/7.
 */
@Component
class SalReturnStockRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findNoPushDown(): MutableList<String>?{
        return namedParameterJdbcTemplate.query("""
        SELECT DISTINCT
            t21.FBILLNO
        FROM
            T_SAL_RETURNSTOCK t21,
            T_SAL_RETURNSTOCKENTRY_F t22,
            T_SAL_RETURNSTOCKENTRY_R t23,
            T_BAS_BILLTYPE_L t24
        WHERE
            t21.FID = t22.FID
        AND t22.FENTRYID = t23.FENTRYID
        AND t22.FPRICE != 0
        AND t23.FSTOCKBASEARJOINQTY = 0
        AND t21.FBILLTYPEID = t24.FBILLTYPEID
        AND t24.FNAME = '标准销售退货单'
        """, BeanPropertyRowMapper(String::class.java))
    }
}
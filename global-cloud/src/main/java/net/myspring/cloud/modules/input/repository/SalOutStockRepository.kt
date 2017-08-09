package net.myspring.cloud.modules.input.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 销售出库单
 * Created by lihx on 2017/6/7.
 */
@Component
class SalOutStockRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findNoPushDown(): MutableList<String>?{
        return namedParameterJdbcTemplate.query("""
        SELECT DISTINCT
            t11.FBILLNO
        FROM
            T_SAL_OUTSTOCK t11,
            T_SAL_OUTSTOCKENTRY_F t12,
            T_SAL_OUTSTOCKENTRY_R t13,
            T_BAS_BILLTYPE_L t14
        WHERE
            t11.FID = t12.FID
        AND t12.FENTRYID = t13.FENTRYID
        AND t12.FPRICE != 0
        AND t13.FARJoinQty = 0
        AND t11.FBILLTYPEID = t14.FBILLTYPEID
        AND t14.FNAME = '标准销售出库单'
        """, BeanPropertyRowMapper(String::class.java))
    }
}

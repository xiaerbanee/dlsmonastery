package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdExpense
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 费用项目
 * Created by lihx on 2017/8/9.
 */
@Component
class BdExpenseRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findAll():MutableList<BdExpense>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t.FEXPID,
                t.FNUMBER,
                tl.FNAME
            FROM
                T_BD_EXPENSE t,
                T_BD_EXPENSE_L tl
            WHERE
                t.FEXPID = tl.FEXPID
        """, BeanPropertyRowMapper(BdExpense::class.java))
    }
}
package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 *  核算维度
 * Created by lihx on 2017/6/22.
 */
@Component
class BdFlexItemPropertyRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdFlexItemProperty>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t.FID,
                t.FNUMBER,
                t1.FNAME,
                t.FFLEXNUMBER
            FROM
                T_BD_FLEXITEMPROPERTY t,
                T_BD_FLEXITEMPROPERTY_L t1
            WHERE
                t.FID = t1.FID
            ORDER BY
                t1.FID
        """, BeanPropertyRowMapper(BdFlexItemProperty::class.java))
    }
}
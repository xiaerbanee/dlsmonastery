package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 核算维度组
 * Created by lihx on 2017/6/16.
 */
@Component
class BdFlexItemGroupRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdFlexItemGroup>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FID,
                t1.FNUMBER,
                t2.FNAME
            FROM
                T_BD_FLEXITEMGROUP t1,
                T_BD_FLEXITEMGROUP_L t2
            WHERE
                t1.FID = t2.FID
            AND t1.FID IN (
                SELECT DISTINCT
                    FITEMDETAILID
                FROM
                    T_BD_ACCOUNT
            )
            ORDER BY
                t1.FID
        """, BeanPropertyRowMapper(BdFlexItemGroup::class.java))
    }
}
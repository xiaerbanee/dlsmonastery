package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.common.config.MyBeanPropertyRowMapper
import net.myspring.cloud.modules.kingdee.domain.BdStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdStockRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByNameLike(name: String): MutableList<BdStock> {
        return jdbcTemplate.query("""
            SELECT
            t1.FSTOCKID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FGROUP,
            t4.FNAME AS groupName,
            t1.FMODIFYDATE
            FROM
            T_BD_STOCK t1,
            T_BD_STOCK_L t2,
            T_BD_STOCKGROUP t3,
            T_BD_STOCKGROUP_L t4
            WHERE
            t1.FSTOCKID = t2.FSTOCKID
            AND t1.FGROUP = t3.FID
            AND t3.FID = t4.FID
            and t2.FNAME like %?%
        """, MyBeanPropertyRowMapper(BdStock::class.java), name)
    }

    fun findByNameList(nameList: MutableList<String>): MutableList<BdStock> {
        return namedParameterJdbcTemplate.query("""
            SELECT
            t1.FSTOCKID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FGROUP,
            t4.FNAME AS groupName,
            t1.FMODIFYDATE
            FROM
            T_BD_STOCK t1,
            T_BD_STOCK_L t2,
            T_BD_STOCKGROUP t3,
            T_BD_STOCKGROUP_L t4
            WHERE
            t1.FSTOCKID = t2.FSTOCKID
            AND t1.FGROUP = t3.FID
            AND t3.FID = t4.FID
            and t2.FNAME in (:nameList)
        """, Collections.singletonMap("nameList",nameList), MyBeanPropertyRowMapper(BdStock::class.java))
    }

    fun findAll(): MutableList<BdStock> {
        return jdbcTemplate.query("""
            SELECT
            t1.FSTOCKID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FGROUP,
            t4.FNAME AS groupName,
            t1.FMODIFYDATE
            FROM
            T_BD_STOCK t1,
            T_BD_STOCK_L t2,
            T_BD_STOCKGROUP t3,
            T_BD_STOCKGROUP_L t4
            WHERE
            t1.FSTOCKID = t2.FSTOCKID
            AND t1.FGROUP = t3.FID
            AND t3.FID = t4.FID
        """, MyBeanPropertyRowMapper(BdStock::class.java))

    }
}
package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdCustomerGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by lihx on 2017/8/14.
 */
@Component
class BdCustomerGroupRepository  @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdCustomerGroup>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FID,
                t1.FNUMBER,
                t2.FNAME
            FROM
                T_BD_CUSTOMERGROUP t1,
                T_BD_CUSTOMERGROUP_L t2
            WHERE
                t1.FID = t2.FID
        """, BeanPropertyRowMapper(BdCustomerGroup::class.java))
    }

    fun findByNameLike(name:String): MutableList<BdCustomerGroup>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                a.FID,
                a.FNUMBER,
                b.FNAME
            FROM
                T_BD_CUSTOMERGROUP a
            JOIN T_BD_CUSTOMERGROUP_L b ON a.FID = b.FID AND b.FLOCALEID = '2052'
            where b.FNAME like concat('%',:name,'%')
        """, Collections.singletonMap("name",name), BeanPropertyRowMapper(BdCustomerGroup::class.java))
    }

}
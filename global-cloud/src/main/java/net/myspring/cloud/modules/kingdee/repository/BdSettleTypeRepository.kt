package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdSettleType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by lihx on 2017/6/9.
 */
@Component
class BdSettleTypeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByNameList(nameList:List<String>):MutableList<BdSettleType>? {
        return namedParameterJdbcTemplate.query("""
            select 
                t1.FID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from 
                T_BD_SETTLETYPE t1,
                T_BD_SETTLETYPE_L t2
            where 
                t1.FID=t2.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t2.FNAME in (:nameList)
        """, Collections.singletonMap("nameList",nameList), BeanPropertyRowMapper(BdSettleType::class.java))
    }

    fun findAll():MutableList<BdSettleType>? {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_SETTLETYPE t1,
                T_BD_SETTLETYPE_L t2
            where
                t1.FID=t2.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
        """, BeanPropertyRowMapper(BdSettleType::class.java))
    }

    fun findAllForDefault():MutableList<BdSettleType>? {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_SETTLETYPE t1,
                T_BD_SETTLETYPE_L t2
            where
                t1.FID=t2.FID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t1.FID in (1,4)
        """, BeanPropertyRowMapper(BdSettleType::class.java))
    }
}
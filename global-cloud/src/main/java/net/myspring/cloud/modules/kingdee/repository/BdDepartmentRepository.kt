package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.common.config.MyBeanPropertyRowMapper
import net.myspring.cloud.modules.kingdee.domain.BdDepartment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdDepartmentRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByIdList(idList: MutableList<String>): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
            t1.FDEPTID,
            t1.FNUMBER,
            t2.FFULLNAME as FNAME
            from
            T_BD_DEPARTMENT t1,
            T_BD_DEPARTMENT_L t2
            where
            t1.FDEPTID = t2.FDEPTID
            and t1.FDEPTID in (:idList)
        """, Collections.singletonMap("idList", idList), MyBeanPropertyRowMapper(BdDepartment::class.java))
    }

    fun findByNameLike(name: String): MutableList<BdDepartment> {
        return jdbcTemplate.query("""
            select
            t1.FDEPTID,
            t1.FNUMBER,
            t2.FFULLNAME as FNAME
            from
            T_BD_DEPARTMENT t1,
            T_BD_DEPARTMENT_L t2
            where
            t1.FDEPTID = t2.FDEPTID
            and t2.FFULLNAME like %?%
        """, MyBeanPropertyRowMapper(BdDepartment::class.java), name)
    }
}
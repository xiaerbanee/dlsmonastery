package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdDepartment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdDepartmentRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findByIdList(idList: MutableList<String>): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FDEPTID in (:idList)
        """, Collections.singletonMap("idList", idList), BeanPropertyRowMapper(BdDepartment::class.java))
    }

    fun findAll(): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
        """, BeanPropertyRowMapper(BdDepartment::class.java))
    }

    fun findByNameLike(name: String): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FFULLNAME like concat('%',:name,'%')
        """, Collections.singletonMap("name",name),BeanPropertyRowMapper(BdDepartment::class.java))
    }

    fun findByNameList(nameList: List<String>): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FFULLNAME in (:nameList)
        """, Collections.singletonMap("nameList",nameList),BeanPropertyRowMapper(BdDepartment::class.java))
    }

    fun findByCustId(custId:String): BdDepartment {
        return namedParameterJdbcTemplate.queryForObject("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FDEPTID in (
                        select t3.FSALDEPTID
                        from T_BD_CUSTOMER t3
                        where t3.FCUSTID = :custId
                    )
        """,Collections.singletonMap("custId",custId), BeanPropertyRowMapper(BdDepartment::class.java))
    }

    //SalDeptId是BdCustomer对象关联BdDepartment的字段
    fun findBySalDeptIdList(salDeptIdList:List<String>): MutableList<BdDepartment> {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FDEPTID,
                t1.FNUMBER,
                t2.FFULLNAME,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            from
                T_BD_DEPARTMENT t1,
                T_BD_DEPARTMENT_L t2
            where
                t1.FDEPTID = t2.FDEPTID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FDEPTID in (:salDeptIdList)
        """,Collections.singletonMap("salDeptIdList",salDeptIdList), BeanPropertyRowMapper(BdDepartment::class.java))
    }
}
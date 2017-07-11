package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdDepartment
import net.myspring.cloud.modules.kingdee.web.query.BdDepartmentQuery
import net.myspring.util.repository.SQLServerDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdDepartmentRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findPageIncludeForbid(pageable: Pageable, bdDepartmentQuery: BdDepartmentQuery): Page<BdDepartment>? {
        var sb = StringBuilder("""
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
        """);
        if(bdDepartmentQuery.departmentIdList.size > 0){
            sb.append(" and t1.FDEPTID in (:departmentIdList) ")
        }
        var pageableSql = SQLServerDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = SQLServerDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(bdDepartmentQuery), BeanPropertyRowMapper(BdDepartment::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(bdDepartmentQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

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

    fun findAllIncludeForbid(): MutableList<BdDepartment> {
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

    fun findByCustId(custId:String): BdDepartment? {
        val list = namedParameterJdbcTemplate.query("""
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
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
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
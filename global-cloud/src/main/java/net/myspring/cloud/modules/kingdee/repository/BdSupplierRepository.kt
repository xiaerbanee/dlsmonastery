package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdSupplier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * 供应商
 * Created by lihx on 2017/6/13.
 */
@Component
class BdSupplierRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByNameLike(name: String): MutableList<BdSupplier>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FSUPPLIERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BD_SUPPLIER t1,
                T_BD_SUPPLIER_L t2
            WHERE
                t1.FSUPPLIERID = t2.FSUPPLIERID 
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t2.FNAME like concat('%',:name,'%')
        """, Collections.singletonMap("name",name), BeanPropertyRowMapper(BdSupplier::class.java))
    }

    fun findByNameList(nameList: List<String>): MutableList<BdSupplier>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FSUPPLIERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BD_SUPPLIER t1,
                T_BD_SUPPLIER_L t2
            WHERE
                t1.FSUPPLIERID = t2.FSUPPLIERID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t2.FNAME in (:nameList)
        """, Collections.singletonMap("nameList",nameList), BeanPropertyRowMapper(BdSupplier::class.java))
    }

    fun findAll(): MutableList<BdSupplier>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FSUPPLIERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BD_SUPPLIER t1,
                T_BD_SUPPLIER_L t2
            WHERE
                t1.FSUPPLIERID = t2.FSUPPLIERID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
        """, BeanPropertyRowMapper(BdSupplier::class.java))
    }

    fun findBySupplierIdList(supplierIdList: List<String>): MutableList<BdSupplier>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FSUPPLIERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            FROM
                T_BD_SUPPLIER t1,
                T_BD_SUPPLIER_L t2
            WHERE
                t1.FSUPPLIERID = t2.FSUPPLIERID
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t1.FSUPPLIERID in (:supplierIdList)
        """,Collections.singletonMap("supplierIdList",supplierIdList), BeanPropertyRowMapper(BdSupplier::class.java))
    }

}
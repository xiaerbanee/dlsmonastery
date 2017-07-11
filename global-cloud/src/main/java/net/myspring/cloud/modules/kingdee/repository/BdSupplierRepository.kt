package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdSupplier
import net.myspring.cloud.modules.kingdee.web.query.BdSupplierQuery
import net.myspring.util.repository.SQLServerDialect
import net.myspring.util.text.StringUtils
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

    fun findPageIncludeForbid(pageable: Pageable, bdSupplierQuery: BdSupplierQuery): Page<BdSupplier>? {
        var sb = StringBuilder("""
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
        """);
        if(bdSupplierQuery.supplierIdList.size > 0){
            sb.append(" and t1.FSUPPLIERID in (:supplierIdList) ")
        }
        var pageableSql = SQLServerDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = SQLServerDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(bdSupplierQuery), BeanPropertyRowMapper(BdSupplier::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(bdSupplierQuery),Long::class.java);
        return PageImpl(list,pageable,count);
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
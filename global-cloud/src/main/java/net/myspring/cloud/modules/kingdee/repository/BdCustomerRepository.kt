package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdCustomer
import net.myspring.cloud.modules.kingdee.web.query.BdCustomerQuery
import net.myspring.common.dto.NameValueDto
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
import java.time.LocalDateTime
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdCustomerRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
        """, BeanPropertyRowMapper(BdCustomer::class.java))
    }

    fun findByNameList(nameList: MutableList<String>): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME in (:nameLists)
        """, Collections.singletonMap("nameLists",nameList), BeanPropertyRowMapper(BdCustomer::class.java))

    }

    fun findByNumberList(numberList: MutableList<String>): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FNUMBER in (:numberList)
        """, Collections.singletonMap("numberList",numberList), BeanPropertyRowMapper(BdCustomer::class.java))
    }

    fun findByNumber(number: String): BdCustomer? {
        var list =  namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FNUMBER = :number
        """, Collections.singletonMap("number",number), BeanPropertyRowMapper(BdCustomer::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findByIdList(idList: MutableList<String>): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FCUSTID in (:idList)
        """, Collections.singletonMap("idList",idList),  BeanPropertyRowMapper(BdCustomer::class.java))
    }

    fun findByNameLike(name: String): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
             FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            where
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                AND t2.FNAME like concat('%',:name,'%')
        """, Collections.singletonMap("name",name),BeanPropertyRowMapper(BdCustomer::class.java))
    }

    fun findPrimaryGroupAndPrimaryGroupName(): MutableList<NameValueDto> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FPRIMARYGROUP as value,
                t4.FNAME AS name
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
            group by
                t1.FPRIMARYGROUP,
                t4.FNAME
        """, BeanPropertyRowMapper(NameValueDto::class.java))
    }

    fun findPageIncludeForbid(pageable: Pageable, bdCustomerQuery: BdCustomerQuery): Page<BdCustomer>? {
        var sb = StringBuilder("""
             SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
        """);
        if(bdCustomerQuery.customerIdList.size > 0){
           sb.append(" and t1.FCUSTID in (:customerIdList) ")
        }
        if (StringUtils.isNotBlank(bdCustomerQuery.customerGroup)){
            sb.append(" and t1.FPRIMARYGROUP = :customerGroup  ")
        }
        var pageableSql = SQLServerDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = SQLServerDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(bdCustomerQuery), BeanPropertyRowMapper(BdCustomer::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(bdCustomerQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

    fun findByMaxModifyDate(modifyDate: LocalDateTime): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FCUSTID,
                t1.FNUMBER,
                t1.FSALDEPTID,
                t2.FNAME,
                t1.FPRIMARYGROUP,
                t4.FNAME AS fprimaryGroupName,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_CUSTOMER t1,
                T_BD_CUSTOMER_L t2,
                T_BD_CUSTOMERGROUP t3,
                T_BD_CUSTOMERGROUP_L t4
            WHERE
                t1.FCUSTID = t2.FCUSTID
                AND t1.FPRIMARYGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FMODIFYDATE > :modifyDate
        """,Collections.singletonMap("modifyDate",modifyDate.toString()), BeanPropertyRowMapper(BdCustomer::class.java))
    }
}

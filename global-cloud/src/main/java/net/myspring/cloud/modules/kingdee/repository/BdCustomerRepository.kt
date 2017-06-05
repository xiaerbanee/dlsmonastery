package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdCustomer
import net.myspring.common.dto.NameValueDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@Component
class BdCustomerRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
            t1.FCUSTID,
            t1.FNUMBER,
            t1.FSALDEPTID,
            t2.FNAME,
            t1.FPRIMARYGROUP,
            t4.FNAME AS fprimaryGroupName,
            t1.FMODIFYDATE
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
            t1.FMODIFYDATE
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

    fun findByIdList(idList: MutableList<String>): MutableList<BdCustomer> {
        return namedParameterJdbcTemplate.query("""
            SELECT
            t1.FCUSTID,
            t1.FNUMBER,
            t1.FSALDEPTID,
            t2.FNAME,
            t1.FPRIMARYGROUP,
            t4.FNAME AS fprimaryGroupName,
            t1.FMODIFYDATE
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

    fun findById(id: String): BdCustomer {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
            t1.FCUSTID,
            t1.FNUMBER,
            t1.FSALDEPTID,
            t2.FNAME,
            t1.FPRIMARYGROUP,
            t4.FNAME AS fprimaryGroupName,
            t1.FMODIFYDATE
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
            and t1.FCUSTID = :id
        """,Collections.singletonMap("id",id),BeanPropertyRowMapper(BdCustomer::class.java))
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
            t1.FMODIFYDATE
             FROM
            T_BD_CUSTOMER t1,
            T_BD_CUSTOMER_L t2,
            T_BD_CUSTOMERGROUP t3,
            T_BD_CUSTOMERGROUP_L t4
            where
            t1.FCUSTID = t2.FCUSTID
            AND t1.FPRIMARYGROUP = t3.FID
            AND t3.FID = t4.FID
            AND t2.FNAME like like concat('%',:name,'%')
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

}


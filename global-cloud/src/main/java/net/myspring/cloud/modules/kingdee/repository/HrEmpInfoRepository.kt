package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.HrEmpInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by lihx on 2017/6/8.
 */
@Component
class  HrEmpInfoRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByNameList(nameList: List<String>): MutableList<HrEmpInfo>? {
        return namedParameterJdbcTemplate.query("""
            select 
                t1.FNUMBER,
                t2.FNAME,
                t1.FForbidStatus,
                t1.FDOCUMENTSTATUS
            from 
                T_HR_EMPINFO t1,
                T_HR_EMPINFO_L t2
            where 
                t1.FID =t2.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME in (:nameList)
        """, Collections.singletonMap("nameList", nameList), BeanPropertyRowMapper(HrEmpInfo::class.java))
    }

    fun findByName(name: String): HrEmpInfo? {
        var list =  namedParameterJdbcTemplate.query("""
            select
                t1.FNUMBER,
                t2.FNAME,
                t1.FForbidStatus,
                t1.FDOCUMENTSTATUS
            from
                T_HR_EMPINFO t1,
                T_HR_EMPINFO_L t2
            where
                t1.FID =t2.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME in (:name)
        """, Collections.singletonMap("name", name), BeanPropertyRowMapper(HrEmpInfo::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findAll(): MutableList<HrEmpInfo>? {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FNUMBER,
                t2.FNAME,
                t1.FForbidStatus,
                t1.FDOCUMENTSTATUS
            from
                T_HR_EMPINFO t1,
                T_HR_EMPINFO_L t2
            where
                t1.FID =t2.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
        """, BeanPropertyRowMapper(HrEmpInfo::class.java))
    }
}
package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by lihx on 2017/6/8.
 */
@Component
class BdAccountRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): MutableList<BdAccount>? {
        return namedParameterJdbcTemplate.query("""
            select
                t1.FACCTID,
                t2.FFULLNAME,
                t1.FNUMBER,
                t2.FNAME,
                t1.FITEMDETAILID,
                t1.FISBANK,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_ACCOUNT t1,
                T_BD_ACCOUNT_L t2
            where
                t1.FACCTID=t2.FACCTID
                and t2.FLOCALEID=2052
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
        """, BeanPropertyRowMapper(BdAccount::class.java))
    }

    fun findByIsBank(isBank:Boolean): MutableList<BdAccount>? {
        return namedParameterJdbcTemplate.query("""
            select 
                t1.FACCTID, 
                t2.FFULLNAME,
                t1.FNUMBER,
                t2.FNAME,
                t1.FITEMDETAILID,
                t1.FISBANK,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from 
                T_BD_ACCOUNT t1,
                T_BD_ACCOUNT_L t2
            where 
                t1.FACCTID=t2.FACCTID 
                and t2.FLOCALEID=2052
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t1.FISBANK = :isBank
        """,Collections.singletonMap("isBank",isBank), BeanPropertyRowMapper(BdAccount::class.java))
    }

    fun findByNumber(number:String):BdAccount?{
        var list =  namedParameterJdbcTemplate.query("""
            select
                t1.FACCTID,
                t2.FFULLNAME,
                t1.FNUMBER,
                t2.FNAME,
                t1.FITEMDETAILID,
                t1.FISBANK,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_ACCOUNT t1,
                T_BD_ACCOUNT_L t2
            where
                t1.FACCTID=t2.FACCTID
                and t2.FLOCALEID=2052
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t1.FNUMBER = :number
        """,Collections.singletonMap("number",number),BeanPropertyRowMapper(BdAccount::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findByName(name:String):BdAccount?{
        var list = namedParameterJdbcTemplate.query("""
            select
                t1.FACCTID,
                t2.FFULLNAME,
                t1.FNUMBER,
                t2.FNAME,
                t1.FITEMDETAILID,
                t1.FISBANK,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_ACCOUNT t1,
                T_BD_ACCOUNT_L t2
            where
                t1.FACCTID=t2.FACCTID
                and t2.FLOCALEID=2052
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t2.FNAME = :name
        """,Collections.singletonMap("name",name),BeanPropertyRowMapper(BdAccount::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findByNameList(nameList:List<String>):MutableList<BdAccount>?{
        return namedParameterJdbcTemplate.query("""
            select
                t1.FACCTID,
                t2.FFULLNAME,
                t1.FNUMBER,
                t2.FNAME,
                t1.FITEMDETAILID,
                t1.FISBANK,
                t1.FDOCUMENTSTATUS,
                t1.FFORBIDSTATUS
            from
                T_BD_ACCOUNT t1,
                T_BD_ACCOUNT_L t2
            where
                t1.FACCTID=t2.FACCTID
                and t2.FLOCALEID=2052
                AND t1.FDOCUMENTSTATUS = 'C'
                AND t1.FFORBIDSTATUS = 'A'
                and t2.FNAME in (:nameList)
        """,Collections.singletonMap("nameList",nameList),BeanPropertyRowMapper(BdAccount::class.java))
    }
}
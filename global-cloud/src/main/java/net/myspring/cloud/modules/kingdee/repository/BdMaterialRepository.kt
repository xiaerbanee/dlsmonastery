package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdMaterial
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*


/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdMaterialRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
        """, BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByErpCleId(ErpCleId:String): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t5.FERPCLSID = :ErpCleId
        """,Collections.singletonMap("ErpCleId",ErpCleId), BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByMaxModifyDate(modifyDate:LocalDateTime): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FMODIFYDATE > :modifyDate
        """,Collections.singletonMap("modifyDate",modifyDate.toString()), BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByName(name: String): BdMaterial? {
        var list = namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME = :name
        """,Collections.singletonMap("name",name),BeanPropertyRowMapper(BdMaterial::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findByNumber(number: String): BdMaterial? {
        var list = namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FNUMBER = :number
        """,Collections.singletonMap("number",number),BeanPropertyRowMapper(BdMaterial::class.java))
        if(list.size>0){
            return list[0]
        }else{
            return null
        }
    }

    fun findByNameList(nameList: List<String>): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME in (:nameList)
        """,Collections.singletonMap("nameList",nameList),BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByMasterIdList(MasterIdList: List<String>): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t5.FERPCLSID,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4,
	            T_BD_MATERIALBASE t5
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                AND t1.FMATERIALID = t5.FMATERIALID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FMASTERID in (:MasterIdList)
        """,Collections.singletonMap("MasterIdList",MasterIdList),BeanPropertyRowMapper(BdMaterial::class.java))
    }
}
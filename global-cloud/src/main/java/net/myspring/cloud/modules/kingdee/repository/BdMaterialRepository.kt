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
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
        """, BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByModifyDate(modifyDate:LocalDateTime): MutableList<BdMaterial> {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FMODIFYDATE > :modifyDate
        """,Collections.singletonMap("modifyDate",modifyDate.toString()), BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByName(name: String): BdMaterial {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME = :name
        """,Collections.singletonMap("name",name),BeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByNumber(number: String): BdMaterial {
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                t1.FMASTERID,
                t1.FNUMBER,
                t2.FNAME,
                t1.FMATERIALGROUP,
                t4.FNAME AS FMaterialGroupName,
                t3.FNUMBER as FMaterialGroupNumber,
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t1.FNUMBER = :number
        """,Collections.singletonMap("number",number),BeanPropertyRowMapper(BdMaterial::class.java))
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
                t1.FMODIFYDATE,
                t1.FFORBIDSTATUS,
                t1.FDOCUMENTSTATUS
            FROM
                T_BD_MATERIAL t1,
                T_BD_MATERIAL_L t2,
                T_BD_MATERIALGROUP t3,
                T_BD_MATERIALGROUP_L t4
            WHERE
                t1.FMATERIALID = t2.FMATERIALID
                AND t1.FMATERIALGROUP = t3.FID
                AND t3.FID = t4.FID
                and t1.FFORBIDSTATUS = 'A'
                and t1.FDOCUMENTSTATUS = 'C'
                and t2.FNAME in (:nameList)
        """,Collections.singletonMap("nameList",nameList),BeanPropertyRowMapper(BdMaterial::class.java))
    }
}
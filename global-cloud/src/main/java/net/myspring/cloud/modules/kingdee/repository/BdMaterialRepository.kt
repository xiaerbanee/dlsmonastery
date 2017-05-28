package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.common.config.MyBeanPropertyRowMapper
import net.myspring.cloud.modules.kingdee.domain.BdMaterial
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


/**
 * Created by haos on 2017/5/24.
 */
@Component
class  BdMaterialRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate){
    fun findAll(): MutableList<BdMaterial> {
        return jdbcTemplate.query("""
            SELECT
            t1.FMASTERID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FMATERIALGROUP,
            t4.FNAME AS fmaterialGroupName,
            t1.FMODIFYDATE
            FROM
            T_BD_MATERIAL t1,
            T_BD_MATERIAL_L t2,
            T_BD_MATERIALGROUP t3,
            T_BD_MATERIALGROUP_L t4
            WHERE
            t1.FMATERIALID = t2.FMATERIALID
            AND t1.FMATERIALGROUP = t3.FID
            AND t3.FID = t4.FID
        """, MyBeanPropertyRowMapper(BdMaterial::class.java))
    }

    fun findByName(name: String): BdMaterial {
        return jdbcTemplate.queryForObject("""
            SELECT
            t1.FMASTERID,
            t1.FNUMBER,
            t2.FNAME,
            t1.FMATERIALGROUP,
            t4.FNAME AS fmaterialGroupName,
            t1.FMODIFYDATE
            FROM
            T_BD_MATERIAL t1,
            T_BD_MATERIAL_L t2,
            T_BD_MATERIALGROUP t3,
            T_BD_MATERIALGROUP_L t4
            WHERE
            t1.FMATERIALID = t2.FMATERIALID
            AND t1.FMATERIALGROUP = t3.FID
            AND t3.FID = t4.FID
            and t2.FNAME = ?
        """,MyBeanPropertyRowMapper(BdMaterial::class.java), name)
    }
}
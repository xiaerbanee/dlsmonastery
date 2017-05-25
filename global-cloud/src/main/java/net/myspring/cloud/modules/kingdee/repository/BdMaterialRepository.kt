package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.BdMaterial
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  BdMaterialRepository{
    @Query("""
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
     """, nativeQuery = true)
    fun findAll():MutableList<BdMaterial>

    @Query("""
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
            and t2.FNAME = :name
     """, nativeQuery = true)
    fun findByName(@Param("name")name:String):BdMaterial
}
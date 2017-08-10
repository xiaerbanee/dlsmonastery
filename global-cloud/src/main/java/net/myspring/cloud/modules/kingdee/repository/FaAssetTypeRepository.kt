package net.myspring.cloud.modules.kingdee.repository

import net.myspring.cloud.modules.kingdee.domain.FaAssetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

/**
 * 资产类别
 * Created by lihx on 2017/8/9.
 */
@Component
class FaAssetTypeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun findAll():MutableList<FaAssetType>? {
        return namedParameterJdbcTemplate.query("""
            SELECT
                t.FID,
                t.FNUMBER,
                tl.FNAME
            FROM
                T_FA_ASSETTYPE t,
                T_FA_ASSETTYPE_L tl
            WHERE
                t.FID = tl.FID
        """, BeanPropertyRowMapper(FaAssetType::class.java))
    }
}
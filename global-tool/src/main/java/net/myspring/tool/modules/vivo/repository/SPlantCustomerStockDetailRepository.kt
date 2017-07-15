package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SPlantCustomerStockDetailRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findCustomerStockDetailData(dateStart:LocalDate,dateEnd:LocalDate) :MutableList<SPlantCustomerStockDetailDto>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            SELECT
                de.area_id AS areaId,
                im.product_id AS productId,
                im.ime AS ime,
                im.depot_id AS storeID,
                im.depot_id AS customerID,
                1 AS customerLevel
            FROM
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            WHERE
                im.depot_id IS NOT NULL
                AND im.depot_id = de.id
                AND im.created_date >= :dateStart
                AND im.created_date <  :dateEnd
                AND (
                        im.retail_date IS NULL
                        OR im.retail_date > :dateEnd
                    )
                AND im.enabled = 1
                AND de.enabled = 1
                AND de.id = de1.depot_id
                AND de1.joint_level = '一级'

            union all
            SELECT
                de.area_id AS areaId,
                im.product_id AS productId,
                im.ime AS ime,
                de.area_id AS storeID,
                de.area_id AS customerID,
                2 AS customerLevel
            FROM
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            WHERE
                im.depot_id IS NOT NULL
                AND im.depot_id = de.id
                AND im.created_date >= :dateStart
                AND im.created_date <  :dateEnd
                AND (
                        im.retail_date IS NULL
                        OR im.retail_date > :dateEnd
                    )
                AND im.enabled = 1
                AND de.enabled = 1
                AND de.id = de1.depot_id
                AND de1.joint_level = '二级'

            union all

            SELECT
                de.area_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                de.area_id as storeID,
                de.id as customerID,
                3 as customerLevel
            FROM
                crm_product_ime im,
                crm_depot de,
                crm_depot_shop shop
            WHERE
                im.created_date >= :dateStart
                and im.created_date < :dateEnd
                and (
                        im.retail_date is null
                        or im.retail_date > :dateEnd
                    )
                and im.depot_id is not null
                and im.depot_id = de.id
                and im.enabled = 1
                and de.enabled = 1
                and shop.depot_id=de.id
                and de.id not in (select depot_id from crm_depot_store)
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(SPlantCustomerStockDetailDto::class.java))

    }
}
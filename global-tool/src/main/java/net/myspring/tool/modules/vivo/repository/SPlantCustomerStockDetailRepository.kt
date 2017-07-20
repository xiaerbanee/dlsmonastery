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
            select
                de.area_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                im.depot_id as storeId,
                im.depot_id as customerId,
                1 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            where
                im.depot_id is not null
                and im.depot_id = de.id
                and im.created_date >= :dateStart
                and im.created_date <  :dateEnd
                and (
                        im.retail_date is null
                        or im.retail_date > :dateEnd
                    )
                and im.enabled = 1
                and de.enabled = 1
                and de.id = de1.depot_id
                and de1.joint_level = '一级'
            union all
            select
                de.area_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                de.area_id as storeId,
                de.area_id as customerId,
                2 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            where
                im.depot_id is not null
                and im.depot_id = de.id
                and im.created_date >= :dateStart
                and im.created_date <  :dateEnd
                and (
                        im.retail_date is null
                        or im.retail_date > :dateEnd
                    )
                and im.enabled = 1
                and de.enabled = 1
                and de.id = de1.depot_id
                and de1.joint_level = '二级'
            union all
            select
                de.area_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                de.area_id as storeID,
                de.id as customerID,
                3 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_shop shop
            where
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

    fun findIDVivoCustomerStockDetailData(dateStart:LocalDate,dateEnd:LocalDate) :MutableList<SPlantCustomerStockDetailDto>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            select
                de.province_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                im.depot_id as storeId,
                im.depot_id as customerId,
                1 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            where
                im.depot_id is not null
                and im.depot_id = de.id
                and im.created_date >= :dateStart
                and im.created_date <  :dateEnd
                and (
                        im.retail_date is null
                        or im.retail_date > :dateEnd
                    )
                and im.enabled = 1
                and de.enabled = 1
                and de.id = de1.depot_id
                and de1.joint_level = '一级'
            union all
            select
                de.province_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                de.area_id as storeId,
                de.area_id as customerId,
                2 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_store de1
            where
                im.depot_id is not null
                and im.depot_id = de.id
                and im.created_date >= :dateStart
                and im.created_date <  :dateEnd
                and (
                        im.retail_date is null
                        or im.retail_date > :dateEnd
                    )
                and im.enabled = 1
                and de.enabled = 1
                and de.id = de1.depot_id
                and de1.joint_level = '二级'
            union all
            select
                de.province_id as areaId,
                im.product_id as productId,
                im.ime as ime,
                de.area_id as storeID,
                de.id as customerID,
                3 as customerLevel
            from
                crm_product_ime im,
                crm_depot de,
                crm_depot_shop shop
            where
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
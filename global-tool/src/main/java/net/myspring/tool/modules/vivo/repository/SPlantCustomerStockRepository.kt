package net.myspring.tool.modules.vivo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SPlantCustomerStockRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findCustomerStockData(dateStart: LocalDate,dateEnd: LocalDate):MutableList<SPlantCustomerStockDto>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            select de.area_id as CustomerId,t.product_id as productId,count(*) as useAbleStock ,'' as agentCode,1 as customerLevel
            from crm_product_ime t ,crm_depot de,crm_depot_store de1
            where t.depot_id=de.id
                and t.created_date >= :dateStart
                and t.created_date < :dateEnd
                and (t.retail_date is null or t.retail_date > :dateEnd)
                and t.enabled =1
                and de.enabled=1
                and de.id=de1.depot_id
                and de1.joint_level='一级'
            GROUP BY de.area_id,t.product_id

            union all

            select de.area_id as CustomerId,im.product_id as productId,count(*) as useAbleStock ,'' as agentCode,2 as customerLevel
            from crm_product_ime im ,crm_depot de,crm_depot_store de1
            where im.depot_id=de.id
                and im.created_date >= :dateStart
                and im.created_date < :dateEnd
                and (im.retail_date is null or im.retail_date > :dateEnd)
                and im.enabled =1
                and de.enabled=1
                and de.id=de1.depot_id
                and de1.joint_level='二级'
            GROUP BY de.area_id,im.product_id

            union all

            select de.id as CustomerId,im.product_id as productId,count(*) as useAbleStock ,'' as agentCode,3 as customerLevel
            from crm_product_ime im ,crm_depot de,crm_depot_shop shop
            where im.depot_id=de.id
                and im.created_date >= :dateStart
                and im.created_date < :dateEnd
                and (im.retail_date is null or im.retail_date > :dateEnd)
                and im.enabled =1
                and de.enabled=1
                and de.id=shop.depot_id
                and de.id not in (select depot_id from crm_depot_store)
            GROUP BY de.id,im.product_id
        """)
        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(SPlantCustomerStockDto::class.java));
    }
}
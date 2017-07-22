package net.myspring.tool.modules.future.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDetailDto
import net.myspring.tool.modules.vivo.dto.SPlantCustomerStockDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class FutureProductImeRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){
    fun findAll(dateStart: String, dateEnd: String,date:String): MutableList<OppoCustomerStock> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("dateEnd", dateEnd);
        paramMap.put("date", date);
        return namedParameterJdbcTemplate.query("""
             select
                  de.id as customerid,
                  pro.id as productcode ,
                  count(*) as qty,
                 :date as date
              from
                crm_product_ime im left join crm_product_ime_upload up on im.product_ime_upload_id = up.id,crm_depot de,crm_product pro
                where
                    im.depot_id is not null
                    and im.depot_id = de.id
                    and im.created_date>=:dateStart
                    and im.created_date<:dateEnd
                    and im.enabled = 1
                    and (
                        im.retail_date is null
                        or im.retail_date >:dateEnd
                    )
                    and (
                        up.id is null or up.created_date > :dateEnd
                    )
                    and pro.enabled=1
                    and im.product_id = pro.id
                    group by de.id,pro.id asc
            """, paramMap, BeanPropertyRowMapper(OppoCustomerStock::class.java));
    }

    fun findCustomerStockData(dateStart: LocalDate, dateEnd: LocalDate):MutableList<SPlantCustomerStockDto>{
        val map = Maps.newHashMap<String,Any>()
        map.put("dateStart",dateStart)
        map.put("dateEnd",dateEnd)
        val sb = StringBuilder()
        sb.append("""
            select de.area_id as CustomerId,t.product_id as productId,count(*) as useAbleStock ,1 as customerLevel
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

            select de.area_id as CustomerId,im.product_id as productId,count(*) as useAbleStock ,2 as customerLevel
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

            select de.id as CustomerId,im.product_id as productId,count(*) as useAbleStock ,3 as customerLevel
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
        return namedParameterJdbcTemplate.query(sb.toString(),map,BeanPropertyRowMapper(SPlantCustomerStockDto::class.java))
    }

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

}
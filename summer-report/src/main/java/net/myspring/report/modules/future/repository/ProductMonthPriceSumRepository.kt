package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.dto.ProductMonthPriceSumDto
import net.myspring.report.modules.future.web.query.ProductMonthPriceSumQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.lang.StringBuilder

@Component
class ProductMonthPriceSumRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findReportData(productMonthPriceSumQuery: ProductMonthPriceSumQuery):MutableList<ProductMonthPriceSumDto>{
        val sb = StringBuilder();
        if(productMonthPriceSumQuery==null||!productMonthPriceSumQuery.isDetail){
            sb.append("""
                SELECT
                    t1.*, t2.office_id,t2.area_id,count(*) qty
            """)
        }
        if(productMonthPriceSumQuery!=null&&productMonthPriceSumQuery.isDetail){
            sb.append("""
                SELECT
                    t1.*, t2.office_id,t2.area_id,t3.ime,t4.name as productName,t5.name as productTypeName,t2.name as shopName
            """)
        }
        sb.append("""
                FROM
                    crm_product_ime_upload t1 left join crm_depot t2 on t1.shop_id=t2.id  and t2.enabled = 1
                    left join crm_product_ime t3 on t1.product_ime_id=t3.id and t3.enabled=1
                    left join crm_product t4 on t3.product_id=t4.id
                    left join crm_product_type t5 on t4.product_type_id=t5.id
               where
	                t1.enabled = 1
                AND
                    t1.month = :month
            """)
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.status)) {
            sb.append(""" AND t1.status = :status """)
        }
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.areaId)) {
            sb.append(""" AND t2.area_id = :areaId """)
        }
        if (CollectionUtil.isNotEmpty(productMonthPriceSumQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        if(productMonthPriceSumQuery==null||!productMonthPriceSumQuery.isDetail){
            sb.append(""" GROUP BY t1.shop_id,t1.employee_id,t1.product_type_id """)
        }
        if(productMonthPriceSumQuery!=null&&productMonthPriceSumQuery.isDetail){
            sb.append("""
               order by t1.shop_id,t1.employee_id,t1.product_type_id
            """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productMonthPriceSumQuery), BeanPropertyRowMapper(ProductMonthPriceSumDto::class.java))
    }
}
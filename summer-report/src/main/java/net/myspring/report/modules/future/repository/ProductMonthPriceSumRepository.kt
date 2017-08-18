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
        sb.append("""
                SELECT
                    t1.*, t2.office_id,t2.area_id,count(*) qty
                FROM
                    crm_product_ime_upload t1 left join crm_depot t2 on t1.shop_id=t2.id  and t2.enabled = 1
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
        sb.append(""" GROUP BY t1.shop_id,t1.employee_id,t1.product_type_id """)
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(productMonthPriceSumQuery), BeanPropertyRowMapper(ProductMonthPriceSumDto::class.java))
    }
}
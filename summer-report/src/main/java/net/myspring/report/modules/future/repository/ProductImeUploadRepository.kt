package net.myspring.report.modules.future.repository

import net.myspring.report.modules.future.web.query.ProductMonthPriceSumQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class ProductImeUploadRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun batchAudit(productMonthPriceSumQuery: ProductMonthPriceSumQuery): Int {
        val sb = StringBuilder("""
                    update
                        crm_product_ime_upload t1,
                        crm_depot t2
                    set t1.status=’已通过‘
                    WHERE
                        t1.enabled =1
                    AND t2.enabled =1
                    AND t1.shop_id = t2.id
                    and t1.status=’申请中‘
                    AND t1.month = :month
                    AND t2.office_id in (:auditOfficeIdList) """)
        return namedParameterJdbcTemplate.update(sb.toString(), BeanPropertySqlParameterSource(productMonthPriceSumQuery))
    }
}
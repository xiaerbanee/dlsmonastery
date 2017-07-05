package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot
import net.myspring.future.modules.crm.dto.AfterSaleDto
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery
import net.myspring.future.modules.crm.web.query.AfterSaleQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface AfterSaleProductAllotRepository : BaseRepository<AfterSaleProductAllot, String>,AfterSaleProductAllotRepositoryCustom {

}


interface AfterSaleProductAllotRepositoryCustom{

    fun findPage(pageable: Pageable, afterSaleProductAllotQuery : AfterSaleProductAllotQuery): Page<AfterSaleProductAllotDto>


}

class AfterSaleProductAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleProductAllotRepositoryCustom {

    override fun findPage(pageable: Pageable, afterSaleProductAllotQuery: AfterSaleProductAllotQuery): Page<AfterSaleProductAllotDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
                t1.*,
                t2.name as "fromProductName",
                t4.name as "toProductName",
                t3.name as "storeName",
                t5.business_id
            FROM
                crm_after_sale_product_allot t1,
                crm_after_sale t5,
                crm_product t2,
                crm_depot t3,
                crm_product t4
            WHERE
                t1.store_id = t3.id
                AND t1.from_product_id = t2.id
                AND t1.to_product_id = t4.id
                and t1.after_sale_id=t5.id
                AND t1.enabled = 1
        """)
        if (afterSaleProductAllotQuery.createdDateStart != null) {
            sb.append(" AND t1.created_date >= :createdDateStart ")
        }
        if (afterSaleProductAllotQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date  < :createdDateEnd ")
        }
        if (afterSaleProductAllotQuery.businessId != null) {
            sb.append(" AND t5.business_id=:businessId ")
        }
        if (CollectionUtil.isNotEmpty(afterSaleProductAllotQuery.toOutCodeList)) {
            sb.append(" AND t1.to_out_code in (:toOutCodeList)")
        }
        if (afterSaleProductAllotQuery.fromProductName != null) {
            sb.append(" AND t2.name LIKE CONCAT('%',:fromProductName,'%')")
        }
        if (afterSaleProductAllotQuery.toProductName != null) {
            sb.append(" AND t4.name LIKE CONCAT('%',:toProductName, '%')")
        }
        if (afterSaleProductAllotQuery.storeName != null) {
            sb.append(" AND t3.name LIKE CONCAT('%',:storeName,'%')")
        }
        if (CollectionUtil.isNotEmpty(afterSaleProductAllotQuery.fromOutCodeList)) {
            sb.append(" AND t1.from_out_code in (:fromOutCodeList) ")
        }

        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(afterSaleProductAllotQuery), BeanPropertyRowMapper(AfterSaleProductAllotDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(afterSaleProductAllotQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}
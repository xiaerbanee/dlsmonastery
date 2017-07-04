package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto
import net.myspring.future.modules.crm.dto.AfterSaleStoreAllotDto
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery
import net.myspring.future.modules.crm.web.query.AfterSaleStoreAllotQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface AfterSaleStoreAllotRepository : BaseRepository<AfterSaleStoreAllot, String>,AfterSaleStoreAllotRepositoryCustom {

}


interface AfterSaleStoreAllotRepositoryCustom{
    fun findPage(pageable: Pageable, afterSaleStoreAllotQuery : AfterSaleStoreAllotQuery): Page<AfterSaleStoreAllotDto>

    fun findFilter(afterSaleStoreAllotQuery : AfterSaleStoreAllotQuery): MutableList<AfterSaleStoreAllotDto>
}

class AfterSaleStoreAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleStoreAllotRepositoryCustom {
    override fun findFilter(afterSaleStoreAllotQuery: AfterSaleStoreAllotQuery): MutableList<AfterSaleStoreAllotDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
                t1.*,
                t2.name as "productName",
                t4.name as "toStoreName",
                t3.name as "fromStoreName",
                t5.business_id
            FROM
                crm_after_sale_store_allot t1,
                crm_product t2,
                crm_depot t3,
                crm_depot t4,
                crm_after_sale t5
            WHERE
                t1.product_id = t2.id
                AND t1.from_store_id = t3.id
                AND t1.to_store_id = t4.id
                and t1.after_sale_id=t5.id
                AND t1.enabled = 1
        """)
        if (afterSaleStoreAllotQuery.createdDateStart != null) {
            sb.append(" AND t1.created_date >= :createdDateStart ")
        }
        if (afterSaleStoreAllotQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date  < :createdDateEnd ")
        }
        if (afterSaleStoreAllotQuery.businessId != null) {
            sb.append(" AND t5.business_id =:businessId")
        }
        if (afterSaleStoreAllotQuery.outCode  != null) {
            sb.append(" AND t1.out_code =:outCode ")
        }
        if (afterSaleStoreAllotQuery.toStoreName != null) {
            sb.append(" AND t4.name LIKE CONCAT('%',:toStoreName,'%')")
        }
        if (afterSaleStoreAllotQuery.fromStoreName != null) {
            sb.append(" AND t3.name LIKE CONCAT('%',:fromStoreName, '%')")
        }
        if (afterSaleStoreAllotQuery.productName != null) {
            sb.append(" AND t2.name LIKE CONCAT('%',:productName,'%')")
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(afterSaleStoreAllotQuery), BeanPropertyRowMapper(AfterSaleStoreAllotDto::class.java));
    }

    override fun findPage(pageable: Pageable, afterSaleStoreAllotQuery: AfterSaleStoreAllotQuery): Page<AfterSaleStoreAllotDto> {
        var sb = StringBuilder()
        sb.append("""
            SELECT
                t1.*,
                t2.name as "productName",
                t4.name as "toStoreName",
                t3.name as "fromStoreName",
                t5.business_id
            FROM
                crm_after_sale_store_allot t1,
                crm_product t2,
                crm_depot t3,
                crm_depot t4,
                crm_after_sale t5
            WHERE
                t1.product_id = t2.id
                AND t1.from_store_id = t3.id
                AND t1.to_store_id = t4.id
                and t1.after_sale_id=t5.id
                AND t1.enabled = 1
        """)
        if (afterSaleStoreAllotQuery.createdDateStart != null) {
            sb.append(" AND t1.created_date >= :createdDateStart ")
        }
        if (afterSaleStoreAllotQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date  < :createdDateEnd ")
        }
        if (afterSaleStoreAllotQuery.businessId != null) {
            sb.append(" AND t5.business_id =:businessId")
        }
        if (afterSaleStoreAllotQuery.outCode  != null) {
            sb.append(" AND t1.out_code =:outCode ")
        }
        if (afterSaleStoreAllotQuery.toStoreName != null) {
            sb.append(" AND t4.name LIKE CONCAT('%',:toStoreName,'%')")
        }
        if (afterSaleStoreAllotQuery.fromStoreName != null) {
            sb.append(" AND t3.name LIKE CONCAT('%',:fromStoreName, '%')")
        }
        if (afterSaleStoreAllotQuery.productName != null) {
            sb.append(" AND t2.name LIKE CONCAT('%',:productName,'%')")
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(afterSaleStoreAllotQuery), BeanPropertyRowMapper(AfterSaleStoreAllotDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(afterSaleStoreAllotQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }
}
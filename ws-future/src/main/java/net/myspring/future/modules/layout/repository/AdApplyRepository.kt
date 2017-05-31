package net.myspring.future.modules.layout.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdApply
import net.myspring.future.modules.layout.dto.AdApplyDto
import net.myspring.future.modules.layout.web.query.AdApplyQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.util.*

/**
 * Created by zhangyf on 2017/5/24.
 */
interface AdApplyRepository : BaseRepository<AdApply,String>,AdApplyRepositoryCustom {

    @Query("""
        SELECT
            t.id
        FROM
            #{#entityName} t
        WHERE
            t.enabled = 1
    """)
    fun findAllId(): MutableList<String>

    @Query("""
        SELECT
            t.id
        FROM
            #{#entityName} t
        WHERE
            t.enabled = 1
    """)
            //TODO 修改该query
    fun findByFilter(map: Map<String, Any>): MutableList<AdApply>

}

interface AdApplyRepositoryCustom{

    fun findByOutGroupIdAndDate(@Param("dateStart") dateStart: LocalDate, @Param("outGroupIds") outGroupIds: MutableList<String>): MutableList<AdApplyDto>

    fun findPage(pageable: Pageable,adApplyQuery: AdApplyQuery): Page<AdApplyDto>
}

class AdApplyRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AdApplyRepositoryCustom{

    override fun findByOutGroupIdAndDate(dateStart: LocalDate,outGroupIds: MutableList<String>): MutableList<AdApplyDto>{
        var params = HashMap<String,Any>()
        params.put("dateStart",dateStart)
        params.put("outGroupIds",outGroupIds)
        return namedParameterJdbcTemplate.query("""
            SELECT
                t.*
            FROM
                crm_ad_apply t,
                crm_product product
            WHERE
                t.enabled = 1
            AND t.product_id = product.id
            AND t.confirm_qty > t.billed_qty
            AND t.created_date > :dateStart
            AND product.out_group_id IN (:outGroupIds)
        """,params,MyBeanPropertyRowMapper(AdApplyDto::class.java))
    }

    override fun findPage(pageable: Pageable,adApplyQuery: AdApplyQuery): Page<AdApplyDto>{
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_ad_apply t1,
                crm_product product
            WHERE
                t1.enabled = 1
            AND t1.product_id = product.id
        """)
        if (StringUtils.isNotEmpty(adApplyQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.createdBy)) {
            sb.append("""  and t1.create_by = :createdBy """)
        }
        if (adApplyQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adApplyQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productName)) {
            sb.append(""" and product.name like CONCAT('%', :productName,'%') """)
        }
        /*if (adApplyQuery.billed != null) {
            sb.append("""
                and t1.billed_qty
            """)
        }*/
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(adApplyQuery), BeanPropertyRowMapper(AdApplyDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(adApplyQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
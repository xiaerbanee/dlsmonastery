package net.myspring.future.modules.layout.repository

import net.myspring.future.common.config.MyBeanPropertyRowMapper
import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.AdApply
import net.myspring.future.modules.layout.dto.AdApplyDto
import net.myspring.future.modules.layout.web.query.AdApplyQuery
import net.myspring.util.collection.CollectionUtil
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


}

interface AdApplyRepositoryCustom{

    fun findByOutGroupIdAndDate(@Param("dateStart") dateStart: LocalDate, @Param("outGroupIds") outGroupIds: MutableList<String>): MutableList<AdApplyDto>

    fun findByFilter(adApplyQuery: AdApplyQuery):MutableList<AdApplyDto>

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
            ORDER BY t.created_date DESC
        """,params,MyBeanPropertyRowMapper(AdApplyDto::class.java))
    }

    override fun findByFilter(adApplyQuery: AdApplyQuery):MutableList<AdApplyDto>{
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
        if (CollectionUtil.isNotEmpty(adApplyQuery.depotIdList)) {
            sb.append("""  and t1.shop_id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(adApplyQuery.officeIdList)) {
            sb.append("""  and t1.shop_id in (select depot.id from crm_depot depot where depot.office_id in (:officeIdList)) """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (adApplyQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adApplyQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productCode)) {
            sb.append(""" and product.code like CONCAT('%', :productCode,'%') """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productName)) {
            sb.append(""" and product.name like CONCAT('%', :productName,'%') """)
        }
        if (adApplyQuery.isBilled != null) {
            if(adApplyQuery.isBilled){
                sb.append("""
                    and t1.billed_qty > 0
                """)
            }else{
                sb.append("""
                    and t1.billed_qty = 0
                """)
            }
        }

        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(adApplyQuery), BeanPropertyRowMapper(AdApplyDto::class.java))
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
        if (CollectionUtil.isNotEmpty(adApplyQuery.depotIdList)) {
            sb.append("""  and t1.shop_id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(adApplyQuery.officeIdList)) {
            sb.append("""  and t1.shop_id in (select depot.id from crm_depot depot where depot.office_id in (:officeIdList)) """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (adApplyQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (adApplyQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productCode)) {
            sb.append(""" and product.code like CONCAT('%', :productCode,'%') """)
        }
        if (StringUtils.isNotEmpty(adApplyQuery.productName)) {
            sb.append(""" and product.name like CONCAT('%', :productName,'%') """)
        }
        if (adApplyQuery.isBilled != null) {
            if(adApplyQuery.isBilled){
                sb.append("""
                    and t1.billed_qty > 0
                """)
            }else{
                sb.append("""
                    and t1.billed_qty = 0
                """)
            }
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(adApplyQuery), BeanPropertyRowMapper(AdApplyDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(adApplyQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
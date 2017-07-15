package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.repository.PriceChangeRepositoryCustom
import net.myspring.future.modules.crm.domain.*
import net.myspring.future.modules.crm.dto.DemoPhoneDto

import net.myspring.future.modules.crm.dto.ExpressOrderDto
import net.myspring.future.modules.crm.dto.PriceChangeDto
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery
import net.myspring.future.modules.crm.web.query.PriceChangeQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.data.repository.query.Param
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager


interface DemoPhoneRepository : BaseRepository<DemoPhone, String>,DemoPhoneRepositoryCustom {

}

interface DemoPhoneRepositoryCustom{

    fun findByFilter(demoPhoneQuery: DemoPhoneQuery):MutableList<DemoPhoneDto>

    fun findPage(pageable: Pageable, demoPhoneQuery : DemoPhoneQuery): Page<DemoPhoneDto>
}

class DemoPhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): DemoPhoneRepositoryCustom {

    override fun findByFilter(demoPhoneQuery: DemoPhoneQuery):MutableList<DemoPhoneDto>{
        val sb = StringBuilder("""
            SELECT
                t2.ime 'ime',
                t1.*
            FROM
                crm_demo_phone t1,
                crm_product_ime t2
            WHERE
                t1.product_ime_id = t2.id
            AND t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(demoPhoneQuery.ime)) {
            sb.append(""" and t2.ime like CONCAT('%', :ime,'%') """)
        }
        if (StringUtils.isNotEmpty(demoPhoneQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(demoPhoneQuery.demoPhoneTypeId)) {
            sb.append("""  and t1.demo_phone_type_id = :demoPhoneTypeId """)
        }
        if (demoPhoneQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (demoPhoneQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd """)
        }

        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(demoPhoneQuery), BeanPropertyRowMapper(DemoPhoneDto::class.java));
    }

    override fun findPage(pageable: Pageable, demoPhoneQuery: DemoPhoneQuery): Page<DemoPhoneDto> {
        val sb = StringBuilder("""
            SELECT
                product.ime ime,
                depot.name shopName,
                depot.area_id areaId,
                t1.*
            FROM
                crm_demo_phone t1
                LEFT JOIN crm_product_ime product ON t1.product_ime_id = product.id
                LEFT JOIN crm_depot depot ON t1.shop_id = depot.id
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(demoPhoneQuery.ime)) {
            sb.append(""" and product.ime like CONCAT('%', :ime,'%') """)
        }
        if (StringUtils.isNotEmpty(demoPhoneQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(demoPhoneQuery.demoPhoneTypeId)) {
            sb.append("""  and t1.demo_phone_type_id = :demoPhoneTypeId """)
        }
        if (demoPhoneQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (demoPhoneQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd """)
        }
        if (CollectionUtil.isNotEmpty(demoPhoneQuery.officeIdList)) {
            sb.append("""  and depot.office_id in (:officeIdList)  """)
        }
        if (CollectionUtil.isNotEmpty(demoPhoneQuery.depotIdList)) {
            sb.append("""  and depot.id in (:depotIdList)  """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(demoPhoneQuery), BeanPropertyRowMapper(DemoPhoneDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(demoPhoneQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }


}
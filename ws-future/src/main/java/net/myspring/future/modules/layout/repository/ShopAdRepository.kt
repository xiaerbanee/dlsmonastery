package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopAd
import net.myspring.future.modules.layout.dto.ShopAdDto
import net.myspring.future.modules.layout.web.query.ShopAdQuery
import net.myspring.util.collection.CollectionUtil
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopAdRepository : BaseRepository<ShopAd,String>,ShopAdRepositoryCustom {

}

interface ShopAdRepositoryCustom{
    fun findShopAdDto(id:String):ShopAdDto

    fun findPage(pageable: Pageable,shopAdQuery: ShopAdQuery): Page<ShopAdDto>

    fun findByFilter(shopAdQuery: ShopAdQuery): MutableList<ShopAdDto>
}

class ShopAdRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopAdRepositoryCustom{
    override fun findShopAdDto(id:String):ShopAdDto{
        return namedParameterJdbcTemplate.queryForObject("""
            SELECT
                depot.name shopName,
                depot.office_id officeId,
                depot.area_id areaId,
                type.name shopAdTypeName,
                type.price shopAdTypePrice,
                type.total_price_type totalPriceType,
                t1.*
            FROM
                crm_shop_ad t1
                LEFT JOIN crm_depot depot ON t1.shop_id=depot.id
                LEFT JOIN crm_shop_ad_type type ON t1.shop_ad_type_id = type.id
            WHERE
                t1.enabled=1
            and t1.id = :id
        """, Collections.singletonMap("id",id),BeanPropertyRowMapper(ShopAdDto::class.java))
    }

    override fun findPage(pageable: Pageable,shopAdQuery: ShopAdQuery): Page<ShopAdDto>{
        val sb = StringBuilder("""
            SELECT
                depot.name shopName,
                depot.office_id officeId,
                depot.area_id areaId,
                type.name shopAdTypeName,
                type.price shopAdTypePrice,
                type.total_price_type totalPriceType,
                t1.*
            FROM
                crm_shop_ad t1
                LEFT JOIN crm_depot depot ON t1.shop_id=depot.id
                LEFT JOIN crm_shop_ad_type type ON t1.shop_ad_type_id = type.id
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(shopAdQuery.shopAdTypeId)) {
            sb.append("""  and t1.shop_ad_type_id = :shopAdTypeId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.shopName)) {
            sb.append("""  and depot.name like CONCAT('%', :shopName,'%') """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.lastModifiedBy)) {
            sb.append("""  and t1.last_modified_by = :lastModifiedBy """)
        }
        if (shopAdQuery.specialArea != null && shopAdQuery.specialArea) {
            sb.append("""  and t1.special_area = 1 """)
        }
        if (shopAdQuery.specialArea != null && !shopAdQuery.specialArea) {
            sb.append("""  and t1.special_area = 0 """)
        }
        if (shopAdQuery.ids != null) {
            sb.append("""  and t1.id in (:ids) """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.areaId)) {
            sb.append("""  and depot.area_id = :areaId """)
        }
        if (shopAdQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (shopAdQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd""")
        }
        if (shopAdQuery.lastModifiedDateStart != null) {
            sb.append("""  and t1.last_modified_date  >= :lastModifiedDateStart """)
        }
        if (shopAdQuery.lastModifiedDateEnd != null) {
            sb.append("""  and t1.last_modified_date  < :lastModifiedDateEnd""")
        }
        if (CollectionUtil.isNotEmpty(shopAdQuery.depotIdList)) {
            sb.append("""  and depot.id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(shopAdQuery.officeIdList)) {
            sb.append("""  and depot.office_id in (:officeIdList) """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(shopAdQuery), BeanPropertyRowMapper(ShopAdDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(shopAdQuery),Long::class.java)
        return PageImpl(list,pageable,count)

    }

    override fun findByFilter(shopAdQuery: ShopAdQuery): MutableList<ShopAdDto>{
        val sb = StringBuilder("""
            SELECT
                depot.name shopName,
                depot.office_id officeId,
                depot.area_id areaId,
                type.name shopAdTypeName,
                type.price shopAdTypePrice,
                type.total_price_type totalPriceType,
                t1.*
            FROM
                crm_shop_ad t1
                LEFT JOIN crm_depot depot ON t1.shop_id=depot.id
                LEFT JOIN crm_shop_ad_type type ON t1.shop_ad_type_id = type.id
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(shopAdQuery.shopAdTypeId)) {
            sb.append("""  and t1.shop_ad_type_id = :shopAdTypeId """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.shopName)) {
            sb.append("""  and depot.name like CONCAT('%', :shopName,'%') """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.lastModifiedBy)) {
            sb.append("""  and t1.last_modified_by = :lastModifiedBy """)
        }
        if (shopAdQuery.specialArea != null && shopAdQuery.specialArea) {
            sb.append("""  and t1.special_area = 1 """)
        }
        if (shopAdQuery.specialArea != null && !shopAdQuery.specialArea) {
            sb.append("""  and t1.special_area = 0 """)
        }
        if (shopAdQuery.ids != null) {
            sb.append("""  and t1.id in (:ids) """)
        }
        if (StringUtils.isNotEmpty(shopAdQuery.areaId)) {
            sb.append("""  and depot.area_id = :areaId """)
        }
        if (shopAdQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date  >= :createdDateStart """)
        }
        if (shopAdQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date  < :createdDateEnd""")
        }
        if (shopAdQuery.lastModifiedDateStart != null) {
            sb.append("""  and t1.last_modified_date  >= :lastModifiedDateStart """)
        }
        if (shopAdQuery.lastModifiedDateEnd != null) {
            sb.append("""  and t1.last_modified_date  < :lastModifiedDateEnd""")
        }
        if (CollectionUtil.isNotEmpty(shopAdQuery.depotIdList)) {
            sb.append("""  and depot.id in (:depotIdList) """)
        }
        if (CollectionUtil.isNotEmpty(shopAdQuery.officeIdList)) {
            sb.append("""  and depot.office_id in (:officeIdList) """)
        }

        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(shopAdQuery), BeanPropertyRowMapper(ShopAdDto::class.java))
    }
}
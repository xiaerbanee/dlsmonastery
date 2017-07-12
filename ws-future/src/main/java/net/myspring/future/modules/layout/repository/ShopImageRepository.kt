package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopImage
import net.myspring.future.modules.layout.dto.ShopImageDto
import net.myspring.future.modules.layout.web.query.ShopImageQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopImageRepository : BaseRepository<ShopImage,String>,ShopImageRepositoryCustom {
}

interface ShopImageRepositoryCustom{
    fun findPage(pageable: Pageable, shopImageQuery: ShopImageQuery): Page<ShopImageDto>
}

class ShopImageRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopImageRepositoryCustom{

    override fun findPage(pageable: Pageable, shopImageQuery: ShopImageQuery): Page<ShopImageDto> {
        val sb = StringBuilder("""
            SELECT
                depot.name shopName,
                depot.office_Id officeId,
                depot.area_id areaId,
                t1.*
            FROM
                crm_shop_image t1,
                crm_depot depot
            WHERE
                t1.enabled = 1
            AND t1.shop_id = depot.id
        """)
        if (StringUtils.isNotEmpty(shopImageQuery.shopName)) {
            sb.append("""  and depot.name LIKE CONCAT('%',:shopName,'%') """)
        }
        if (StringUtils.isNotEmpty(shopImageQuery.areaId)) {
            sb.append("""  and depot.area_id = :areaId """)
        }
        if (shopImageQuery.createdDateStart != null) {
            sb.append(" AND t1.created_date >= :createdDateStart ")
        }
        if (shopImageQuery.createdDateEnd != null) {
            sb.append(" AND t1.created_date  < :createdDateEnd ")
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(shopImageQuery), BeanPropertyRowMapper(ShopImageDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(shopImageQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopPromotion
import net.myspring.future.modules.layout.dto.ShopPromotionDto
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopPromotionRepository : BaseRepository<ShopPromotion,String>,ShopPromotionRepositoryCustom {

    @Query("""
    SELECT
        MAX(t1.businessId)
    FROM
        #{#entityName} t1
    WHERE
        t1.createdDate >= ?1
    """)
    fun findMaxBusinessId(localDateTime: LocalDateTime): String
}

interface ShopPromotionRepositoryCustom{
    fun findPage(pageable: Pageable, shopPromotionQuery: ShopPromotionQuery): Page<ShopPromotionDto>
}

class ShopPromotionRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopPromotionRepositoryCustom{

    override fun findPage(pageable: Pageable, shopPromotionQuery: ShopPromotionQuery): Page<ShopPromotionDto> {

        val sb = StringBuilder("""
            SELECT
                depot.name shopName,
                t1.*
            FROM
                crm_shop_promotion t1
                LEFT JOIN crm_depot depot ON depot.id = t1.shop_id
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(shopPromotionQuery.shopName)) {
            sb.append("""  and depot.name LIKE CONCAT('%',:shopName,'%') """)
        }
        if (StringUtils.isNotEmpty(shopPromotionQuery.businessId)) {
            sb.append("""  and t1.business_id LIKE CONCAT('%',:businessId,'%') """)
        }
        if (StringUtils.isNotEmpty(shopPromotionQuery.activityType)) {
            sb.append("""  and t1.activity_type = :activityType """)
        }
        if (shopPromotionQuery.createdDateStart!=null) {
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if (shopPromotionQuery.createdDateEnd!=null) {
            sb.append("""  and t1.created_date < :createdDateEnd """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql =MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql,BeanPropertySqlParameterSource(shopPromotionQuery),BeanPropertyRowMapper(ShopPromotionDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql,BeanPropertySqlParameterSource(shopPromotionQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
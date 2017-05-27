package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopBuild
import net.myspring.future.modules.layout.dto.ShopAllotDto
import net.myspring.future.modules.layout.dto.ShopBuildDto
import net.myspring.future.modules.layout.web.query.ShopAllotQuery
import net.myspring.future.modules.layout.web.query.ShopBuildQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.persistence.EntityManager

/**
 * Created by zhangyf on 2017/5/24.
 */
interface ShopBuildRepository : BaseRepository<ShopBuild,String>,ShopBuildRepositoryCustom {

}

interface ShopBuildRepositoryCustom{
    fun findPage(pageable: Pageable, shopBuildQuery: ShopBuildQuery): Page<ShopBuildDto>

    fun findByFilter(shopBuildQuery: ShopBuildQuery): MutableList<ShopBuildDto>
}

class ShopBuildRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopBuildRepositoryCustom{

    override fun findPage(pageable: Pageable, shopBuildQuery: ShopBuildQuery): Page<ShopBuildDto> {
        val sb = StringBuilder("""
            SELECT
                depot.office_id officeId,
                t1.*
            FROM
                crm_shop_build t1,
                crm_depot depot
            WHERE
                t1.enabled = 1
            AND t1.shop_id = depot.id
        """)
        if (StringUtils.isNotEmpty(shopBuildQuery.fixtureType)) {
            sb.append("""  and t1.fixture_type = :fixtureType """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.positionId)) {
            sb.append("""  and t1.process_position_id = :positionId """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId """)
        }
        if (shopBuildQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if (shopBuildQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date < :createdDateEnd """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(shopBuildQuery), BeanPropertyRowMapper(ShopBuildDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(shopBuildQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }

    override fun findByFilter(shopBuildQuery: ShopBuildQuery): MutableList<ShopBuildDto> {
        TODO("findList")
        /*val sb = StringBuffer()
        sb.append("""
            SELECT
                depot.office_id officeId,
                t1.*
            FROM
                crm_shop_build t1,
                crm_depot depot
            WHERE
                t1.enabled = 1
            AND t1.shop_id = depot.id
        """)
        if (StringUtils.isNotEmpty(shopBuildQuery.fixtureType)) {
            sb.append("""  and t1.fixture_type = :fixtureType """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.shopId)) {
            sb.append("""  and t1.shop_id = :shopId """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.positionId)) {
            sb.append("""  and t1.process_position_id = :positionId """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }
        if (StringUtils.isNotEmpty(shopBuildQuery.officeId)) {
            sb.append("""  and depot.office_id = :officeId """)
        }
        if (shopBuildQuery.createdDateStart != null) {
            sb.append("""  and t1.created_date >= :createdDateStart """)
        }
        if (shopBuildQuery.createdDateEnd != null) {
            sb.append("""  and t1.created_date < :createdDateEnd """)
        }
        var query = entityManager.createNativeQuery(sb.toString(), ShopBuildDto::class.java)

        return query.resultList as MutableList<ShopBuildDto>*/
    }
}
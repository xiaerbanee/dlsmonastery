package net.myspring.future.modules.layout.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.layout.domain.ShopPrint
import net.myspring.future.modules.layout.dto.ShopPrintDto
import net.myspring.future.modules.layout.web.query.ShopPrintQuery
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
interface ShopPrintRepository : BaseRepository<ShopPrint,String>,ShopPrintRepositoryCustom {

}

interface ShopPrintRepositoryCustom{
    fun findPage(pageable: Pageable, shopPrintQuery: ShopPrintQuery): Page<ShopPrintDto>
}

class ShopPrintRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):ShopPrintRepositoryCustom{

    override fun findPage(pageable: Pageable, shopPrintQuery: ShopPrintQuery): Page<ShopPrintDto> {
        val sb = StringBuilder("""
            SELECT
                t1.*
            FROM
                crm_shop_print t1
            WHERE
                t1.enabled = 1
        """)
        if (StringUtils.isNotEmpty(shopPrintQuery.officeId)) {
            sb.append("""  and t1.office_id = :officeId """)
        }
        if (StringUtils.isNotEmpty(shopPrintQuery.printType)) {
            sb.append("""  and t1.print_type = :printType """)
        }
        if (StringUtils.isNotEmpty(shopPrintQuery.processStatus)) {
            sb.append("""  and t1.process_status = :processStatus """)
        }
        if (StringUtils.isNotEmpty(shopPrintQuery.createdBy)) {
            sb.append("""  and t1.created_by = :createdBy """)
        }

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(shopPrintQuery), BeanPropertyRowMapper(ShopPrintDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(shopPrintQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeOfficeQuery
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

/**
 * Created by zhangyf on 2017/5/24.
 */
interface DemoPhoneTypeOfficeRepository : BaseRepository<DemoPhoneTypeOffice,String>,DemoPhoneTypeOfficeRepositoryCustom{

    fun findByDemoPhoneTypeId(demoPhoneTypeId: String): MutableList<DemoPhoneTypeOffice>
}

interface DemoPhoneTypeOfficeRepositoryCustom{
    fun findPage(pageable: Pageable, demoPhoneTypeQuery: DemoPhoneTypeOfficeQuery): Page<DemoPhoneTypeOfficeDto>
}

class DemoPhoneTypeOfficeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):DemoPhoneTypeOfficeRepositoryCustom{

    override fun findPage(pageable: Pageable, demoPhoneTypeOfficeQuery: DemoPhoneTypeOfficeQuery): Page<DemoPhoneTypeOfficeDto> {
        val sb = StringBuilder("""
        SELECT
            count(t2.id) AS realQty,
            t1.*
        FROM
            crm_demo_phone_type_office t1
        LEFT JOIN crm_depot depot ON t1.office_id = depot.area_id
        LEFT JOIN crm_demo_phone t2 ON depot.id = t2.shop_id
        AND t1.demo_phone_type_id = t2.demo_phone_type_id
        """)
        if (StringUtils.isNotEmpty(demoPhoneTypeOfficeQuery.officeId)) {
            sb.append("""  where t1.office_id =:officeId """)
        }
        sb.append(""" GROUP BY t1.id """)

        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(demoPhoneTypeOfficeQuery), BeanPropertyRowMapper(DemoPhoneTypeOfficeDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(demoPhoneTypeOfficeQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}
package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.EmployeePhone
import net.myspring.future.modules.basic.dto.EmployeePhoneDto
import net.myspring.future.modules.basic.web.query.EmployeePhoneQuery
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

interface EmployeePhoneRepository : BaseRepository<EmployeePhone, String>,EmployeePhoneRepositoryCustom {

}

interface EmployeePhoneRepositoryCustom{

    fun findPage(pageable: Pageable, employeePhoneQuery: EmployeePhoneQuery): Page<EmployeePhoneDto>

}

class EmployeePhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):EmployeePhoneRepositoryCustom{

    override fun findPage(pageable: Pageable, employeePhoneQuery: EmployeePhoneQuery): Page<EmployeePhoneDto> {
        val sb = StringBuilder("""
             SELECT
                t1.*
            FROM
                 hr_employee_phone t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(employeePhoneQuery.status)) {
            sb.append("""  and t1.status=:status """)
        }
        if (CollectionUtil.isNotEmpty(employeePhoneQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        if (StringUtils.isNotEmpty(employeePhoneQuery.depotName)) {
            sb.append("""
            and t1.depot_id=(
                select id
                from crm_depot
                where enabled=1
                and name=:depotName
            )
            """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(employeePhoneQuery), BeanPropertyRowMapper(EmployeePhoneDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(employeePhoneQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}

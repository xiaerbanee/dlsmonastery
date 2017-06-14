package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.EmployeePhoneDeposit
import net.myspring.future.modules.basic.dto.EmployeePhoneDepositDto
import net.myspring.future.modules.basic.web.query.EmployeePhoneDepositQuery
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

interface EmployeePhoneDepositRepository : BaseRepository<EmployeePhoneDeposit, String>,EmployeePhoneDepositRepositoryCustom {

}

interface EmployeePhoneDepositRepositoryCustom{

    fun findPage(pageable: Pageable, employeePhoneDepositQuery: EmployeePhoneDepositQuery): Page<EmployeePhoneDepositDto>

}

class EmployeePhoneDepositRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):EmployeePhoneDepositRepositoryCustom{

    override fun findPage(pageable: Pageable, employeePhoneDepositQuery: EmployeePhoneDepositQuery): Page<EmployeePhoneDepositDto> {
        val sb = StringBuilder("""
             SELECT
                t1.*
            FROM
                 hr_employee_phone_deposit t1
            WHERE
                t1.enabled=1
        """)
        if (StringUtils.isNotEmpty(employeePhoneDepositQuery.status)) {
            sb.append("""  and t1.status=:status """)
        }
        if (CollectionUtil.isNotEmpty(employeePhoneDepositQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        if (StringUtils.isNotEmpty(employeePhoneDepositQuery.depotName)) {
            sb.append("""
            and t1.depot_id=(
                select id
                from crm_depot
                where enabled=1
                and name=:depotName
            )
            """)
        }
        if (StringUtils.isNotEmpty(employeePhoneDepositQuery.remarks)) {
            sb.append("""  and t1.remarks=:remarks """)
        }
        if (employeePhoneDepositQuery.createdDateStart!=null) {
            sb.append("""  and t1.created_date>=:createdDateStart """)
        }
        if (employeePhoneDepositQuery.createdDateEnd!=null) {
            sb.append("""  and t1.created_date<=:createdDateEnd """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(employeePhoneDepositQuery), BeanPropertyRowMapper(EmployeePhoneDepositDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(employeePhoneDepositQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}

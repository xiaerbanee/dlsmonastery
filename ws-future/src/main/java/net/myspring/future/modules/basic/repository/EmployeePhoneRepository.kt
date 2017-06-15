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

    fun findFilter(employeePhoneQuery: EmployeePhoneQuery): MutableList<EmployeePhoneDto>

}

class EmployeePhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):EmployeePhoneRepositoryCustom{
    override fun findFilter(employeePhoneQuery: EmployeePhoneQuery): MutableList<EmployeePhoneDto> {
        val sb = StringBuilder("""
             SELECT
                t1.*,t2.name as 'depotName',t2.area_id as 'areaId',t3.name as productName,t4.name as 'productTypeName'
            FROM
                 crm_employee_phone t1,crm_depot t2,crm_product t3,crm_product_type t4
            WHERE
                t1.enabled=1
                and t1.depot_id=t2.id
               and t1.product_id=t3.id
                and t3.product_type_id=t4.id
        """)
        if (StringUtils.isNotEmpty(employeePhoneQuery.status)) {
            sb.append("""  and t1.status=:status """)
        }
        if (CollectionUtil.isNotEmpty(employeePhoneQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        if (StringUtils.isNotEmpty(employeePhoneQuery.depotName)) {
            sb.append("""
                and t2.name like concat('%',:depotName,'%')
            """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertySqlParameterSource(employeePhoneQuery), BeanPropertyRowMapper(EmployeePhoneDto::class.java))
    }

    override fun findPage(pageable: Pageable, employeePhoneQuery: EmployeePhoneQuery): Page<EmployeePhoneDto> {
        val sb = StringBuilder("""
             SELECT
                t1.*,t2.name as 'depotName',t2.area_id as 'areaId',t3.name as productName
            FROM
                 crm_employee_phone t1,crm_depot t2,crm_product t3
            WHERE
                t1.enabled=1
                and t1.depot_id=t2.id
               and t1.product_id=t3.id
        """)
        if (StringUtils.isNotEmpty(employeePhoneQuery.status)) {
            sb.append("""  and t1.status=:status """)
        }
        if (CollectionUtil.isNotEmpty(employeePhoneQuery.depotIdList)) {
            sb.append("""  and t1.depot_id in (:depotIdList) """)
        }
        if (StringUtils.isNotEmpty(employeePhoneQuery.depotName)) {
            sb.append("""
                and t2.name like concat('%',:depotName,'%')
            """)
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sb.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(employeePhoneQuery), BeanPropertyRowMapper(EmployeePhoneDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(employeePhoneQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}

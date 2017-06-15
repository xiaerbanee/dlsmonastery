package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Employee
import net.myspring.basic.modules.hr.dto.EmployeeDto
import net.myspring.basic.modules.hr.web.query.EmployeeQuery
import net.myspring.basic.modules.sys.dto.RoleDto
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/25.
 */
@CacheConfig(cacheNames = arrayOf("employees"))
interface EmployeeRepository : BaseRepository<Employee,String>,EmployeeRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): Employee

    @CachePut(key="#p0.id")
    fun save(position: Employee): Employee


    fun findByEnabledIsTrueAndNameContaining(name: String): MutableList<Employee>


    fun findByEnabledIsTrueAndNameIn(nameList: MutableList<String>): MutableList<Employee>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.name IN ?1
    """)
    fun findByNameList(employeeNameList: MutableList<String>): MutableList<Employee>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.status=:status
        and t1.regularDate > :regularDate
    """)
    fun findByStatusAndregularDate(@Param("status") status: String, @Param("regularDate") regularDate: LocalDate): MutableList<Employee>

    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.id in ?1
    """)
    fun findByIds(ids: MutableList<String>): MutableList<Employee>

}
interface EmployeeRepositoryCustom{
    fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto>
}
class EmployeeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): EmployeeRepositoryCustom{
    override fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto> {
        var sb = StringBuilder("""
            SELECT employee.*,account.office_id,account.position_id,account.leader_id
            FROM hr_employee employee,
            hr_account account,
            sys_office office
            where  employee.account_id=account.id
            and account.office_id=office.id
            and employee.enabled=1
        """);
        if(StringUtils.isNotBlank(employeeQuery.name)) {
            sb.append(" and employee.name like CONCAT('%',:name,'%')");
        }
        if(StringUtils.isNotBlank(employeeQuery.status)) {
            sb.append(" and employee.status =:status");
        }
        if(StringUtils.isNotBlank(employeeQuery.mobilePhone)) {
            sb.append(" and employee.mobile_phone like CONCAT('%',:mobilePhone,'%')");
        }
        if(employeeQuery.entryDateStart!=null) {
            sb.append(" AND employee.entry_date > :entryDateStart");
        }
        if(employeeQuery.entryDateEnd!=null) {
            sb.append(" AND employee.entry_date < :entryDateEnd");
        }
        if(employeeQuery.regularDateStart!=null) {
            sb.append(" AND employee.regular_date > :regularDateStart");
        }
        if(employeeQuery.leaveDateStart!=null) {
            sb.append(" AND employee.regular_date < :leaveDateStart");
        }
        if(employeeQuery.regularDateStart!=null) {
            sb.append(" AND employee.leave_date > :regularDateStart");
        }
        if(employeeQuery.leaveDateEnd!=null) {
            sb.append(" AND employee.leave_date < :leaveDateEnd");
        }
        if(employeeQuery.positionId!=null) {
            sb.append(" AND employee.account_id in(select t1.id from hr_account t1 where t1.enabled=1 and t1.position_id=:positionId)");
        }
        if(StringUtils.isNotBlank(employeeQuery.officeId)) {
            sb.append(" and employee.office_id =:officeId");
        }
        if(StringUtils.isNotBlank(employeeQuery.leaderName)) {
            sb.append(" AND employee.account_id in(select t1.id from hr_account t1,hr_account t2 where t1.leader_id=t2.id and t2.login_name=:leaderName and t1.enabled=1)");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(employeeQuery), BeanPropertyRowMapper(EmployeeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(employeeQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

}

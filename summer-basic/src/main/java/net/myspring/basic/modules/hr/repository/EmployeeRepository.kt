package net.myspring.basic.modules.hr.repository

import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Employee
import net.myspring.basic.modules.hr.dto.EmployeeDto
import net.myspring.basic.modules.hr.web.query.EmployeeQuery
import net.myspring.util.collection.CollectionUtil
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
import java.util.*

/**
 * Created by lihx on 2017/5/25.
 */
@CacheConfig(cacheNames = arrayOf("employees"))
interface EmployeeRepository : BaseRepository<Employee,String>,EmployeeRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): Employee

    @CachePut(key="#p0.id")
    fun save(position: Employee): Employee


    fun findByEnabledIsTrueAndNameLike(name: String): MutableList<Employee>


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

}
interface EmployeeRepositoryCustom{
    fun findByNameLike(name: String):MutableList<EmployeeDto>

    fun findFilter(employeeQuery: EmployeeQuery):MutableList<EmployeeDto>

    fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto>

    fun findDtoByIdList(idList:MutableList<String>):MutableList<EmployeeDto>

    fun findAllWorking(dateEnd: LocalDate): MutableList<EmployeeDto>
}
class EmployeeRepositoryImpl @Autowired constructor(val jdbcTemplate: JdbcTemplate, val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): EmployeeRepositoryCustom{
    override fun findAllWorking(dateEnd: LocalDate): MutableList<EmployeeDto> {
        var sb = StringBuilder("""
            SELECT employee.*,account.login_name as accountName,office.name as officeName,position.name as positionName,leader.login_name as leaderName,area.name as areaName
            FROM hr_employee employee left join hr_account account on  employee.account_id=account.id
            left join sys_office office on  account.office_id=office.id
            left join sys_office area on  office.area_id=area.id
            left join hr_position position on  account.position_id=position.id
            left join hr_account leader on  account.leader_id=leader.id
           where employee.leave_date is null or employee.leave_date < :dateEnd and employee.enabled=1
        """);
        var paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateEnd",dateEnd)
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap,BeanPropertyRowMapper(EmployeeDto::class.java))
    }

    override fun findDtoByIdList(idList: MutableList<String>): MutableList<EmployeeDto> {
        var sb = StringBuilder("""
            SELECT employee.*,account.login_name as accountName,office.name as officeName,position.name as positionName,leader.login_name as leaderName,area.name as areaName
            FROM hr_employee employee left join hr_account account on  employee.account_id=account.id
            left join sys_office office on  account.office_id=office.id
            left join sys_office area on  office.area_id=area.id
            left join hr_position position on  account.position_id=position.id
            left join hr_account leader on  account.leader_id=leader.id
            where
             employee.enabled=1
            and employee.id in (:idList)
        """);
        var paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("idList",idList)
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap,BeanPropertyRowMapper(EmployeeDto::class.java))
    }

    override fun findByNameLike(name: String):MutableList<EmployeeDto>{
        return namedParameterJdbcTemplate.query("""
                SELECT
                    account.position_id positionId,
                    pos.name positionName,
                    account.office_id officeId,
                    office.name officeName,
                    t1.*
                FROM
                    hr_employee t1
                LEFT JOIN hr_account account ON account.id = t1.account_id
                LEFT JOIN sys_office office ON account.office_id=office.id
                LEFT JOIN hr_position pos ON  account.position_id=pos.id
                WHERE
                    t1.enabled = 1
                AND t1.name LIKE CONCAT('%' ,:name, '%')
        """,Collections.singletonMap("name",name),BeanPropertyRowMapper(EmployeeDto::class.java))
    }

    override fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto> {
        var sb = StringBuilder("""
            SELECT employee.*,account.login_name as accountName,office.name as officeName,position.name as positionName,leader.login_name as leaderName,area.name as areaName
            FROM hr_employee employee left join hr_account account on  employee.account_id=account.id
            left join sys_office office on  account.office_id=office.id
            left join sys_office area on  office.area_id=area.id
            left join hr_position position on  account.position_id=position.id
            left join hr_account leader on  account.leader_id=leader.id
            where
             employee.enabled=1
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
        if(employeeQuery.regularDateEnd!=null) {
            sb.append(" AND employee.regular_date < :regularDateEnd");
        }
        if(employeeQuery.leaveDateStart!=null) {
            sb.append(" AND employee.leave_date > :leaveDateStart");
        }
        if(employeeQuery.leaveDateEnd!=null) {
            sb.append(" AND employee.leave_date < :leaveDateEnd");
        }
        if(employeeQuery.positionId!=null) {
            sb.append(" AND position.id=:positionId ");
        }
        if(StringUtils.isNotBlank(employeeQuery.officeId)) {
            sb.append(" and account.office_id =:officeId");
        }
        if(CollectionUtil.isNotEmpty(employeeQuery.officeIdList)) {
            sb.append(" and account.office_id in (:officeIdList)");
        }
        if(StringUtils.isNotBlank(employeeQuery.leaderName)) {
            sb.append(" AND leader.login_name like concat('%',:leaderName,'%')");
        }
        if(StringUtils.isNotBlank(employeeQuery.areaId)) {
            sb.append(" AND area.id=:areaId");
        }
        if(employeeQuery.leaveDateMonth!=null) {
            sb.append(" AND ((employee.entry_date<=:leaveDateMonthEnd  and employee.leave_date is null) or employee.leave_date >=:leaveDateMonthStart)");
        }
        var pageableSql = MySQLDialect.getInstance().getPageableSql(sb.toString(),pageable);
        var countSql = MySQLDialect.getInstance().getCountSql(sb.toString());
        var list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(employeeQuery), BeanPropertyRowMapper(EmployeeDto::class.java));
        var count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(employeeQuery),Long::class.java);
        return PageImpl(list,pageable,count);
    }

    override fun findFilter(employeeQuery: EmployeeQuery): MutableList<EmployeeDto> {
        var sb = StringBuilder("""
            SELECT employee.*,account.login_name as accountName,office.name as officeName,position.name as positionName,leader.login_name as leaderName,area.name as areaName
            FROM hr_employee employee left join hr_account account on  employee.account_id=account.id
            left join sys_office office on  account.office_id=office.id
            left join sys_office area on  office.area_id=area.id
            left join hr_position position on  account.position_id=position.id
            left join hr_account leader on  account.leader_id=leader.id
            where
             employee.enabled=1
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
        if(employeeQuery.regularDateEnd!=null) {
            sb.append(" AND employee.regular_date < :regularDateEnd");
        }
        if(employeeQuery.leaveDateStart!=null) {
            sb.append(" AND employee.leave_date > :leaveDateStart");
        }
        if(employeeQuery.leaveDateEnd!=null) {
            sb.append(" AND employee.leave_date < :leaveDateEnd");
        }
        if(employeeQuery.positionId!=null) {
            sb.append(" AND position.id=:positionId ");
        }
        if(StringUtils.isNotBlank(employeeQuery.officeId)) {
            sb.append(" and account.office_id =:officeId");
        }
        if(CollectionUtil.isNotEmpty(employeeQuery.officeIdList)) {
            sb.append(" and account.office_id in (:officeIdList)");
        }
        if(StringUtils.isNotBlank(employeeQuery.leaderName)) {
            sb.append(" AND leader.login_name like concat('%',:leaderName,'%')");
        }
        if(StringUtils.isNotBlank(employeeQuery.areaId)) {
            sb.append(" AND area.id=:areaId");
        }
        if(employeeQuery.leaveDateMonth!=null) {
            sb.append(" AND ((employee.entry_date<=:leaveDateMonthEnd  and employee.leave_date is null) or employee.leave_date >=:leaveDateMonthStart)");
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(employeeQuery),BeanPropertyRowMapper(EmployeeDto::class.java))
    }


}

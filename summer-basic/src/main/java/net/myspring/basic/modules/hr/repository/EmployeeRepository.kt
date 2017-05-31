package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Employee
import net.myspring.basic.modules.hr.dto.EmployeeDto
import net.myspring.basic.modules.hr.web.query.EmployeeQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.LocalDateTime
import javax.persistence.EntityManager

/**
 * Created by lihx on 2017/5/25.
 */
@CacheConfig(cacheNames = arrayOf("employees"))
interface EmployeeRepository : BaseRepository<Employee,String>,EmployeeRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): Employee

    @CachePut(key="#id")
    fun save(position: Employee): Employee


    fun findByEnabledIsTrueAndNameLike(name: String): MutableList<Employee>


    fun findByEnabledIsTrueAndNameIn(nameList: MutableList<String>): MutableList<Employee>


    @Query("""
        SELECT t1
        FROM #{#entityName} t1
        where t1.enabled=1
        and t1.name like %?1%
    """)
    fun findByNameLike(name: String): MutableList<Employee>

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
        and t1.regularDate>:regularDate
    """)
    fun findByStatusAndregularDate(@Param("status") status: String, @Param("regularDate") regularDate: LocalDateTime): MutableList<Employee>

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

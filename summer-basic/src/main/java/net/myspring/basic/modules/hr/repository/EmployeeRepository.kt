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
    fun save(position: Employee): Int

    @Query("""
        SELECT t1.*
        FROM hr_employee t1
        where t1.enabled=1
        and t1.name like %?1%
    """, nativeQuery = true)
    fun findByNameLike(name: String): List<Employee>

    @Query("""
        SELECT t1.*
        FROM hr_employee t1
        where t1.enabled=1
        and t1.name IN ?1
    """, nativeQuery = true)
    fun findByNameList(employeeNameList: List<String>): List<Employee>

    @Query("""
        SELECT t1.*
        FROM  hr_employee t1
        where t1.enabled=1
        and t1.status=:status
        and t1.regular_date>:regularDate
    """, nativeQuery = true)
    fun findByStatusAndregularDate(@Param("status") status: String, @Param("regularDate") regularDate: LocalDateTime): List<Employee>

    @Query("""
        SELECT t1.*
        FROM  hr_employee t1
        where t1.id in ?1
    """, nativeQuery = true)
    fun findByIds(ids: List<String>): List<Employee>

}
interface EmployeeRepositoryCustom{
    fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto>
}
class EmployeeRepositoryImpl @Autowired constructor(val entityManager: EntityManager): EmployeeRepositoryCustom{
    override fun findPage(pageable: Pageable, employeeQuery: EmployeeQuery): Page<EmployeeDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

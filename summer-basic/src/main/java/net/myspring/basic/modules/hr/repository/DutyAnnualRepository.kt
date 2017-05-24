package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.DutyAnnual
import org.springframework.data.jpa.repository.Query

/**
 * Created by lihx on 2017/5/24.
 */
interface DutyAnnualRepository : BaseRepository<DutyAnnual,String>{
    @Query("""
        select t1.*
        from hr_duty_annual t1
        where t1.employee_id=?1
        order by annual_year desc
        limit 0,1
    """, nativeQuery = true)
    fun findByEmployee(employeeId: String): DutyAnnual

}
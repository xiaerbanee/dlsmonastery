package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountDto
import org.springframework.data.jpa.repository.Query

interface AccountDtoRepository {

    @Query("""
        SELECT
        t1.*,
        t2.leave_date as 'leaveDate',
        t3.role_id as 'roleId'
        FROM
        hr_account t1,
        hr_employee t2,
        hr_position t3
        WHERE
        t1.employee_id = t2.id
        and t1.position_id=t3.id
        and t1.login_name= ?1
        """, nativeQuery = true)
    fun findByLoginName(loginName: String): AccountDto

    @Query("""
        SELECT
        t1.*,
        t2.leave_date as 'leaveDate',
        t3.role_id as 'roleId'
        FROM
        hr_account t1,
        hr_employee t2,
        hr_position t3
        WHERE
        t1.employee_id = t2.id
        and t1.position_id=t3.id
        and t1.id= ?1
        """, nativeQuery = true)
    fun findById(id: String): AccountDto
}

package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class AccountDtoRepository @Autowired constructor(val entityManager: EntityManager) {
    fun findByLoginName(loginName: String): AccountDto {
        return entityManager.createNativeQuery("""
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
                    and t1.login_name= :loginName
                """,AccountDto::class.java).setParameter("loginName",loginName).firstResult as AccountDto;
    }

    fun findById(id: String): AccountDto {
        return entityManager.createNativeQuery("""
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
                    and t1.id= :id
                """,AccountDto::class.java).setParameter("id",id).firstResult as AccountDto;
    }
}
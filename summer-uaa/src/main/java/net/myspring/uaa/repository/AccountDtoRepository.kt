package net.myspring.uaa.repository

import net.myspring.uaa.config.MyBeanPropertyRowMapper
import net.myspring.uaa.dto.AccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class AccountDtoRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate) {

    fun findByLoginName(loginName: String): AccountDto {
        var accountDto = jdbcTemplate.queryForObject("""
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
                    and t1.login_name= ?
                """, MyBeanPropertyRowMapper<AccountDto>(AccountDto::class.java),loginName)
                return accountDto;
    }

    fun findById(id: String): AccountDto {
        return jdbcTemplate.queryForObject("""
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
                    and t1.id= ?
                """,MyBeanPropertyRowMapper<AccountDto>(AccountDto::class.java),id);
    }
}
package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountDtoRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByLoginName(loginName: String): AccountDto {
        var accountDto = namedParameterJdbcTemplate.queryForObject("""
                    SELECT
                    t1.*,
                    t2.leave_date,
                    t3.role_id
                    FROM
                    hr_account t1,
                    hr_employee t2,
                    hr_position t3
                    WHERE
                    t1.employee_id = t2.id
                    and t1.position_id=t3.id
                    and t1.login_name= :loginName
                """, Collections.singletonMap("loginName",loginName), BeanPropertyRowMapper(AccountDto::class.java))
                return accountDto;
    }

    fun findById(id: String): AccountDto {
        return namedParameterJdbcTemplate.queryForObject("""
                    SELECT
                    t1.*,
                    t2.leave_date ,
                    t3.role_id
                    FROM
                    hr_account t1,
                    hr_employee t2,
                    hr_position t3
                    WHERE
                    t1.employee_id = t2.id
                    and t1.position_id=t3.id
                    and t1.id= :id
                """,Collections.singletonMap("id",id),BeanPropertyRowMapper(AccountDto::class.java))
    }
}
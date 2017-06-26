package net.myspring.uaa.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompanyConfigRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findByCode(code: String): String {
        return namedParameterJdbcTemplate.queryForObject("""
              SELECT
                t1.value
                FROM
                sys_company_config t1
                WHERE
                t1.enabled=1
                and t1.code= :code
                """, Collections.singletonMap("code",code),String::class.java);
    }
}
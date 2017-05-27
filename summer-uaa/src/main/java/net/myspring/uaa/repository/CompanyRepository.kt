package net.myspring.uaa.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompanyRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findNameById(id: String): String {
        return namedParameterJdbcTemplate.queryForObject("""
              SELECT
                t1.name
                FROM
                sys_company t1
                WHERE
                t1.enabled=1
                and t1.id= :id
                """, Collections.singletonMap("id",id),String::class.java);
    }

}
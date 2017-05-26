package net.myspring.uaa.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class CompanyRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate) {
    fun findNameById(id: String): String {
        return jdbcTemplate.queryForObject("""
              SELECT
                t1.name
                FROM
                sys_company t1
                WHERE
                t1.enabled=1
                and t1.id= ?
                """,String::class.java,id);
    }

}
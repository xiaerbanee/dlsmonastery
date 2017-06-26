package net.myspring.uaa.repository

import net.myspring.uaa.dto.OfficeDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class OfficeRuleRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findDtoById(id: String): OfficeDto {
        return namedParameterJdbcTemplate.queryForObject("""
              SELECT
                t1.*
                FROM
                sys_office t1
                WHERE
                t1.enabled=1
                and t1.id= :id
                """, Collections.singletonMap("id",id),OfficeDto::class.java);
    }
}
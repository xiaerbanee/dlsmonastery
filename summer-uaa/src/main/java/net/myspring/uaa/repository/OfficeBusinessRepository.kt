package net.myspring.uaa.repository

import net.myspring.uaa.dto.OfficeBusinessDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class OfficeBusinessRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findByOfficeId(officeId: String): MutableList<OfficeBusinessDto> {
        return namedParameterJdbcTemplate.query("""
                    SELECT t1.*
                    FROM  sys_office_business t1
                    where t1.enabled=1
                    and t1.office_id=:officeId
                """, Collections.singletonMap("officeId",officeId), BeanPropertyRowMapper(OfficeBusinessDto::class.java));
    }
}
package net.myspring.uaa.repository

import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class PositionRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByIds(idList: List<String>): MutableList<String> {
        var sql = """
             SELECT
                t1.role_id
                FROM
                hr_position t1
                WHERE
                t1.enabled=1
                and t1.id in (:idList)
        """;
        return namedParameterJdbcTemplate.queryForList(sql, Collections.singletonMap("idList",idList), String::class.java)
    }
}
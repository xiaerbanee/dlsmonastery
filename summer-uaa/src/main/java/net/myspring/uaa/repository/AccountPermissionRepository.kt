package net.myspring.uaa.repository

import com.google.common.collect.Maps
import net.myspring.uaa.dto.OfficeBusinessDto
import net.myspring.uaa.dto.OfficeDto
import net.myspring.uaa.dto.PermissionDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountPermissionRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findPermissionIdByAccountId(accountId:String): MutableList<String> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t.permission_id
            FROM  hr_account_permission t
            where t.enabled=1
            and t.account_id=:accountId
        """)
        var paramMap = Maps.newHashMap<String, String>();
        paramMap.put("accountId",accountId)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap,BeanPropertyRowMapper(String::class.java))
    }
}
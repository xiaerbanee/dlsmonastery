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
class PermissionRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findAllEnabled(): MutableList<PermissionDto> {
        var sb = StringBuilder();
        sb.append("""
             SELECT
                t1.*
                FROM
                sys_permission t1
                WHERE
                t1.enabled=1
        """)
        return namedParameterJdbcTemplate.query(sb.toString(), BeanPropertyRowMapper(PermissionDto::class.java))
    }

    fun findByRoleIdList(roleIdList:List<String>): MutableList<PermissionDto> {
        var sb = StringBuilder();
        sb.append("""
            SELECT t1.*
        FROM
            sys_permission t1,
            sys_role_permission t2
        WHERE
            t1.id = t2.permission_id
        AND t2.role_id in (:roleIdList)
        AND t1.enabled = 1
        AND t2.enabled = 1
        """)
        var paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("roleIdList",roleIdList)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap,BeanPropertyRowMapper(PermissionDto::class.java))
    }

    fun findByAccountId(accountId:String): MutableList<PermissionDto> {
        var sb = StringBuilder();
        sb.append("""
                  SELECT t1.*
        FROM
            sys_permission t1,
            hr_account_permission t3
        WHERE
            t1.id = t3.permission_id
        and t3.account_id=:accountId
        AND t1.enabled = 1
        AND t3.enabled = 1
        """)
        var paramMap = Maps.newHashMap<String, String>();
        paramMap.put("accountId",accountId)
        return namedParameterJdbcTemplate.query(sb.toString(), paramMap,BeanPropertyRowMapper(PermissionDto::class.java))
    }
}
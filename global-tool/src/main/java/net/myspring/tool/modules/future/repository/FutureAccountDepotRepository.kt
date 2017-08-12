package net.myspring.tool.modules.future.repository

import net.myspring.tool.modules.future.dto.AccountDepotDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class FutureAccountDepotRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findAll():MutableList<AccountDepotDto>{
        return namedParameterJdbcTemplate.query("select * from crm_account_depot",BeanPropertyRowMapper(AccountDepotDto::class.java))
    }
}
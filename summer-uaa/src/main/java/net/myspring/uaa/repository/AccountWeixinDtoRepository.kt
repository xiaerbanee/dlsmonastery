package net.myspring.uaa.repository

import net.myspring.uaa.dto.AccountWeixinDto
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountWeixinDtoRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findByOpenId(openId: String): MutableList<AccountWeixinDto> {
        return namedParameterJdbcTemplate.query("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.open_id = :openId
                    and t1.enabled=1
                """, Collections.singletonMap("openId",openId),BeanPropertyRowMapper(AccountWeixinDto::class.java));
    }

    fun findByAccountId(accountId: String): AccountWeixinDto {
        return namedParameterJdbcTemplate.queryForObject("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.account_id = :accountId
                    and t1.enabled=1
                """, Collections.singletonMap("accountId",accountId), BeanPropertyRowMapper(AccountWeixinDto::class.java));
    }

    fun save(accountWeixinDto: AccountWeixinDto) {
        if(StringUtils.isBlank(accountWeixinDto.id)) {
            namedParameterJdbcTemplate.update("INSERT  INTO hr_account_weixin(account_id,company_id,open_id) VALUE (:accountId,:companyId,:openId)",BeanPropertySqlParameterSource(accountWeixinDto));
        } else {
            namedParameterJdbcTemplate.update("UPDATE  hr_account_weixin SET acount_id=:accountId,company_id=:companyId,openId=:openId where id=:id",BeanPropertySqlParameterSource(accountWeixinDto));
        }
    }
}
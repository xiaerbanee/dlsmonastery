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
                    t1.*,t2.login_name as 'accountName'
                    FROM
                    hr_account_weixin t1,hr_account t2
                    WHERE
                    t1.open_id = :openId
                    and t1.enabled=1
                    and t1.account_id=t2.id
                """, Collections.singletonMap("openId",openId),BeanPropertyRowMapper(AccountWeixinDto::class.java));
    }

    fun findByAccountId(accountId: String): MutableList<AccountWeixinDto> {
        return namedParameterJdbcTemplate.query("""
                    SELECT
                    t1.*
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.account_id = :accountId
                    and t1.enabled=1
                """, Collections.singletonMap("accountId",accountId), BeanPropertyRowMapper(AccountWeixinDto::class.java));
    }

    fun save(accountWeixinDto: AccountWeixinDto) {
        if(StringUtils.isBlank(accountWeixinDto.id)) {
            namedParameterJdbcTemplate.update("INSERT  INTO hr_account_weixin(account_id,open_id) VALUE (:accountId,:openId)",BeanPropertySqlParameterSource(accountWeixinDto));
        } else {
            namedParameterJdbcTemplate.update("UPDATE  hr_account_weixin SET account_id=:accountId,open_id=:openId where id=:id",BeanPropertySqlParameterSource(accountWeixinDto));
        }
    }
}
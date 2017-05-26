package net.myspring.uaa.repository

import net.myspring.uaa.config.MyBeanPropertyRowMapper
import net.myspring.uaa.dto.AccountWeixinDto
import net.myspring.util.repository.QueryUtils
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class AccountWeixinDtoRepository @Autowired constructor(val jdbcTemplate: JdbcTemplate,val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findByOpenId(openId: String): MutableList<AccountWeixinDto> {
        return jdbcTemplate.query("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.open_id = ?
                    and t1.enabled=1
                """, MyBeanPropertyRowMapper(AccountWeixinDto::class.java),openId);
    }

    fun findByAccountId(accountId: String): AccountWeixinDto {
        return jdbcTemplate.queryForObject("""
                    SELECT
                    t1.company_id,
                    t1.account_id,
                    t1.open_id
                    FROM
                    hr_account_weixin t1
                    WHERE
                    t1.account_id = :accountId
                    and t1.enabled=1
                """, MyBeanPropertyRowMapper(AccountWeixinDto::class.java),accountId);
    }

    fun save(accountWeixinDto: AccountWeixinDto) {
        var paramMap = QueryUtils.getParamMap(accountWeixinDto);
        if(StringUtils.isBlank(accountWeixinDto.id)) {
            namedParameterJdbcTemplate.update("INSERT  INTO hr_account_weixin(account_id,company_id,open_id) VALUE (:accountId,:companyId,:openId)",paramMap);
        } else {
            namedParameterJdbcTemplate.update("UPDATE  hr_account_weixin SET acount_id=:accountId,company_id=:companyId,openId=:openId where id=:id",paramMap);
        }
    }
}
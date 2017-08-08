package net.myspring.future.modules.basic.repository

import com.google.common.collect.Maps
import net.myspring.future.modules.basic.web.form.AccountDepotForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountDepotRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByAccountId(accountId: String): MutableList<String> {
        return namedParameterJdbcTemplate.queryForList("""
                    SELECT
                    t1.depot_id
                    FROM
                    crm_account_depot t1
                    WHERE
                    t1.account_id = :accountId
                """, Collections.singletonMap("accountId",accountId), String::class.java)
    }

    fun deleteByAccountId(accountId: String): Int {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("accountId", accountId);
        return namedParameterJdbcTemplate.update("""
                    delete from crm_account_depot where account_id=:accountId
                """, MapSqlParameterSource(paramMap))
    }

    fun save(accountDepotForm: AccountDepotForm): Int {
        val sb = StringBuilder();
        sb.append("INSERT INTO crm_account_depot(account_id,depot_id) values")
        for (depotId in accountDepotForm.depotIdList) {
            sb.append("(" + accountDepotForm.accountId + "," + depotId + "),")
        }
        sb.deleteCharAt(sb.length - 1)
        return namedParameterJdbcTemplate.update(sb.toString(), HashMap<String, Any>())
    }
}
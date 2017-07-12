package net.myspring.basic.modules.hr.repository

import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.Account
import net.myspring.basic.modules.hr.domain.AccountPermission
import net.myspring.basic.modules.hr.dto.AccountChangeDto
import net.myspring.basic.modules.hr.web.form.AccountChangeForm
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Created by lihx on 2017/5/24.
 */
interface AccountPermissionRepository : BaseRepository<AccountPermission,String>,AccountPermissionRepositoryCustom{
   

    @Query("""
        update  #{#entityName} t set t.enabled=?1 where t.permissionId IN ?2 and t.accountId=?3
        """)
    @Modifying
    fun setEnabledByAccountAndPermissionIdList(enabled: Boolean,permissionIdList: MutableList<String>,accountId: String): Int

    @Query("""
        SELECT t.permissionId
        FROM  #{#entityName} t
        where t.enabled=1
        and t.accountId=?1
        """)
    fun findPermissionIdByAccountId(account: String): MutableList<String>

}

interface AccountPermissionRepositoryCustom {

    fun setEnabledByAccountId(enabled:Boolean,accountId: String): Int
}


class AccountPermissionRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate):AccountPermissionRepositoryCustom {
    override fun setEnabledByAccountId(enabled: Boolean, accountId: String): Int {
        var sb = StringBuilder()
        sb.append("""
           update hr_account_permission t1 set t1.enabled=:enabled where t1.account_id=:accountId
        """)
        var paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("enabled",enabled);
        paramMap.put("accountId",accountId);
        return namedParameterJdbcTemplate.update(sb.toString(),paramMap);
    }


}
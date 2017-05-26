package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountWeixin
import org.springframework.data.jpa.repository.Query

/**
 * Created by lihx on 2017/5/24.
 */
interface AccountWeixinRepository : BaseRepository<AccountWeixin,String>{

    @Query("""
        SELECT t1.*
        FROM  #{#entityName} t1
        where t1.enabled=1
        and t1.accountId=?1
    """)
    fun findByAccountId(accountId: String): AccountWeixin

}

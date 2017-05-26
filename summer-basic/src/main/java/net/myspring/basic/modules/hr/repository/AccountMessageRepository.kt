package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountMessage
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

/**
 * Created by lihx on 2017/5/24.
 */

interface AccountMessageRepository : BaseRepository<AccountMessage, String> {

    @Query("""
       SELECT t
        FROM  #{#entityName} t
         where t.enabled=1
         and t.to_accountId=?1
         and t.createdDate >=?2
         and t.readed = 0
        """)
    fun findByAccountId(accountId: String,createdDateStart: LocalDateTime): MutableList<AccountMessage>


}
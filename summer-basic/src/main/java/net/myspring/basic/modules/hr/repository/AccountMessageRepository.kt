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
       SELECT t1.*
         FROM hr_account_message t1
         where t1.enabled=1
         and t1.to_account_id=?1
         and t1.created_date >=?2
         and t1.readed = 0
        """, nativeQuery = true)
    fun findByAccountId(accountId: String,createdDateStart: LocalDateTime): List<AccountMessage>


}
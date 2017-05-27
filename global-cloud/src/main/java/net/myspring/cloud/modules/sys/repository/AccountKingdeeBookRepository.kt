package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  AccountKingdeeBookRepository : BaseRepository<AccountKingdeeBook,String>{
    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled = 1
        and t.accountId = :accountId
     """)
    fun findByAccountId (@Param("accountId")accountId:String): AccountKingdeeBook

}
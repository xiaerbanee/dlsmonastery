package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  AccountKingdeeBookRepository{
    @Query("""
        select t1.*
        from sys_account_kingdee_book t1
        where t1.enabled = 1
        and t1.account_id = :accountId
     """, nativeQuery = true)
    fun findByAccountId (@Param("accountId")accountId:String): AccountKingdeeBook

}
package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.KingdeeBook
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  KingdeeBookRepository:BaseRepository<KingdeeBook,String>{
    @Query("""
        select t1.*
        from sys_kingdee_book t1,sys_account_kingdee_book t2
        where t1.enabled = 1
        and t2.enabled=1
        and t1.id = t2.kingdee_book_id
        and t2.account_id = ?1}
     """, nativeQuery = true)
    fun findByAccountId(accountId:String): KingdeeBook

    @Query("""
        select t1.name
        from sys_kingdee_book t1
        where t1.enabled = 1
     """, nativeQuery = true)
    fun findNames():List<String>

    @Query("""
        select distinct t1.type
        from sys_kingdee_book t1
        where t1.enabled = 1
     """, nativeQuery = true)
    fun findTypes():List<String>
}
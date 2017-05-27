package net.myspring.cloud.modules.sys.repository

import net.myspring.cloud.common.repository.BaseRepository
import net.myspring.cloud.modules.sys.domain.KingdeeBook
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by haos on 2017/5/24.
 */
interface  KingdeeBookRepository : BaseRepository<KingdeeBook,String>{
    @Query("""
        select t1
        FROM  #{#entityName} t1,AccountKingdeeBook t2
        where t1.enabled = 1
        and t2.enabled=1
        and t1.id = t2.kingdeeBookId
        and t2.accountId = :accountId
     """)
    fun findByAccountId(@Param("accountId")accountId:String): KingdeeBook

    @Query("""
        SELECT t1.name
         FROM  #{#entityName} t1
        where t1.enabled = 1
     """)
    fun findNames():MutableList<String>

    @Query("""
        SELECT distinct t1.type
        FROM  #{#entityName} t1
        where t1.enabled = 1
     """)
    fun findTypes():MutableList<String>
}
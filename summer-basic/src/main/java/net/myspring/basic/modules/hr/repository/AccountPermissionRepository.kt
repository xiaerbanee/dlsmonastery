package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountPermission
import org.springframework.data.jpa.repository.Query

/**
 * Created by lihx on 2017/5/24.
 */
interface AccountPermissionRepository : BaseRepository<AccountPermission,String>{
    @Query("""
        DELETE  FROM  #{#entityName} t where t.permissionId IN ?1
        """)
    fun removeByPermissionList(permissionIdList: MutableList<String>): Int

    @Query("""
        SELECT t.permissionId
        FROM  #{#entityName} t
        where t.enabled=1
        and t.accountId=?1
        """)
    fun findPermissionIdByAccount(account: String): MutableList<String>

}
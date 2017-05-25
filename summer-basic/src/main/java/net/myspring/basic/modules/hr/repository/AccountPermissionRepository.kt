package net.myspring.basic.modules.hr.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.hr.domain.AccountPermission
import org.springframework.data.jpa.repository.Query

/**
 * Created by lihx on 2017/5/24.
 */
interface AccountPermissionRepository : BaseRepository<AccountPermission,String>{
    @Query("""
        DELETE  FROM hr_account_permission where permission_id IN ?1
        """, nativeQuery = true)
    fun removeByPermissionList(permissionIdList: MutableList<String>): Int

    @Query("""
         SELECT t1.permission_id
        FROM hr_account_permission t1
        where t1.enabled=1
        and t1.account_id=?1
        """, nativeQuery = true)
    fun findPermissionIdByAccount(account: String): MutableList<String>

}
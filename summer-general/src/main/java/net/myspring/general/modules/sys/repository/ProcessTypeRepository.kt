package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessType
import org.springframework.data.jpa.repository.Query

interface ProcessTypeRepository : BaseRepository<ProcessType, String> {

    fun findByName(name: String): ProcessType


    @Query("""
        SELECT t1.*
        FROM sys_process_type t1
        WHERE t1.audit_file_type=1
        AND t1.enabled=1
        """, nativeQuery = true)
    fun findEnabledAuditFileType(): MutableList<ProcessType>


    @Query("""
        SELECT
            t1.`name`
        FROM
            hr_position_permission t,
            hr_position t1
        WHERE
        t.permission_id = ?1
        and t.position_id = t1.id
        """, nativeQuery = true)
    fun findPositionNames(permissionId: String): MutableList<String>

}
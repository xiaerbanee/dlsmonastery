package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessFlow
import org.springframework.data.jpa.repository.Query

interface ProcessFlowRepository : BaseRepository<ProcessFlow, String> {

    fun findByProcessTypeIdAndName(processTypeId: String, name: String): ProcessFlow

    fun findByProcessTypeId(processTypeId: String): List<ProcessFlow>


    @Query("""
        SELECT t1.*
        FROM  sys_process_flow t1,sys_process_type t2
        where t1.process_type_id=t2.id
        and t2.enabled=1
        and t2.name=?1
        """, nativeQuery = true)
    fun findByProcessTypeName(processTypeName: String): List<ProcessFlow>


}
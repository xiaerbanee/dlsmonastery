package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessFlow
import org.springframework.data.jpa.repository.Query

interface ProcessFlowRepository : BaseRepository<ProcessFlow, String> {

    fun findByProcessTypeIdAndName(processTypeId: String, name: String): ProcessFlow

    fun findByProcessTypeId(processTypeId: String): MutableList<ProcessFlow>

    @Query("select t from #{#entityName} t1,ProcessType t2 where t1.processTypeId=t2.id and t2.enabled=1 and t2.name=?1")
    fun findByProcessTypeName(processTypeName: String): MutableList<ProcessFlow>
}
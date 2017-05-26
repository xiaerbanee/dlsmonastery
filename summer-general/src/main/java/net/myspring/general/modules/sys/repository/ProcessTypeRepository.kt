package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessType
import org.springframework.data.jpa.repository.Query

interface ProcessTypeRepository : BaseRepository<ProcessType, String> {

    fun findByName(name: String): ProcessType

    @Query("select t from #{#entityName} t where t.auditFileType=1 and t.enabled=1")
    fun findEnabledAuditFileType(): MutableList<ProcessType>

}
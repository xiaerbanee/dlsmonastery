package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.FolderFile
import net.myspring.general.modules.sys.domain.ProcessType
import net.myspring.general.modules.sys.dto.ProcessTypeDto
import net.myspring.general.modules.sys.web.query.FolderFileQuery
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

interface ProcessTypeRepository : BaseRepository<ProcessType, String>,ProcessTypeRepositoryCustom {

    fun findByName(name: String): ProcessType

    @Query("select t from #{#entityName} t where t.auditFileType=1 and t.enabled=1")
    fun findEnabledAuditFileType(): MutableList<ProcessType>

}

interface ProcessTypeRepositoryCustom {
    fun findAll(pageable: Pageable, processTypeQuery: ProcessTypeQuery): Page<ProcessType>;
}

class ProcessTypeRepositoryImpl:ProcessTypeRepositoryCustom {
    override fun findAll(pageable: Pageable, processTypeQuery: ProcessTypeQuery): Page<ProcessType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
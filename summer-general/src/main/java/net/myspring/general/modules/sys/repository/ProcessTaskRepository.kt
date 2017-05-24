package net.myspring.general.modules.sys.repository

import net.myspring.general.common.repository.BaseRepository
import net.myspring.general.modules.sys.domain.ProcessTask

interface ProcessTaskRepository : BaseRepository<ProcessTask, String> {

    fun findByProcessInstanceId(processInstanceId: String): ProcessTask


}
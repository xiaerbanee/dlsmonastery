package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.SimpleProcessType


interface SimpleProcessTypeRepository : BaseRepository<SimpleProcessType, String>{

    fun findByEnabledIsTrueAndName(name :String) :SimpleProcessType
}

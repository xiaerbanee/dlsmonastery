package net.myspring.future.modules.basic.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.basic.domain.SimpleProcessStep


interface SimpleProcessStepRepository : BaseRepository<SimpleProcessStep, String>{

    fun findTopByEnabledIsTrueAndSimpleProcessTypeIdOrderBySortAsc(simpleProcessTypeId :String) :SimpleProcessStep

    fun findByEnabledIsTrueAndSimpleProcessTypeIdOrderBySortAsc(simpleProcessTypeId :String) :List<SimpleProcessStep>

}

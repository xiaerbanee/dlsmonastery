package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleFlee


interface AfterSaleFleeRepository : BaseRepository<AfterSaleFlee, String> {


    fun findByEnabledIsTrueAndImeIn(imeList: MutableList<String>): MutableList<AfterSaleFlee>

    fun findByEnabledIsTrueAndAfterSaleIdIn(afterSaleIdList: MutableList<String>): MutableList<AfterSaleFlee>
}



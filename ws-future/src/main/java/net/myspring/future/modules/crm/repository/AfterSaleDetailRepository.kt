package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleDetail


interface AfterSaleDetailRepository : BaseRepository<AfterSaleDetail, String> {


     fun findByAfterSaleIdInAndType(afterSaleId: MutableList<String>, type: String): MutableList<AfterSaleDetail>


}

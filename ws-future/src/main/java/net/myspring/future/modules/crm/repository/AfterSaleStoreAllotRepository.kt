package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface AfterSaleStoreAllotRepository : BaseRepository<AfterSaleStoreAllot, String>,AfterSaleStoreAllotRepositoryCustom {

}


interface AfterSaleStoreAllotRepositoryCustom{

}

class AfterSaleStoreAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleStoreAllotRepositoryCustom {
}
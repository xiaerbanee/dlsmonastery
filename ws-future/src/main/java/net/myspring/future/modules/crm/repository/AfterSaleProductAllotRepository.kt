package net.myspring.future.modules.crm.repository

import net.myspring.future.common.repository.BaseRepository
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface AfterSaleProductAllotRepository : BaseRepository<AfterSaleProductAllot, String>,AfterSaleProductAllotRepositoryCustom {

}


interface AfterSaleProductAllotRepositoryCustom{

}

class AfterSaleProductAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): AfterSaleProductAllotRepositoryCustom {
}
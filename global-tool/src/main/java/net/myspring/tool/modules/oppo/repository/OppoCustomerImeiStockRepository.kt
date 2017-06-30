package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerImeiStock
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerImeiStockRepository : BaseRepository<OppoCustomerImeiStock, String>, OppoCustomerImeiStockRepositoryCustom {

}
interface OppoCustomerImeiStockRepositoryCustom{
}
class OppoCustomerImeiStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerImeiStockRepositoryCustom{

}

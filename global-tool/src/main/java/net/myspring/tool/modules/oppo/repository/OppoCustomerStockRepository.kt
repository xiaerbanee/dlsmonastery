package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerStockRepository : BaseRepository<OppoCustomerStock, String>, OppoCustomerStockRepositoryCustom {

}
interface OppoCustomerStockRepositoryCustom{
}
class OppoCustomerStockRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerStockRepositoryCustom{

}

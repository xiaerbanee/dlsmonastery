package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleRepository : BaseRepository<OppoCustomerSale, String>, OppoCustomerSaleRepositoryCustom {

}
interface OppoCustomerSaleRepositoryCustom{
}
class OppoCustomerSaleRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleRepositoryCustom{

}

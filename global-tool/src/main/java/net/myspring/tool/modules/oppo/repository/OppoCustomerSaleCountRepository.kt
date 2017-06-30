package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSale
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleCountRepository : BaseRepository<OppoCustomerSaleCount, String>, OppoCustomerSaleCountRepositoryCustom {

}
interface OppoCustomerSaleCountRepositoryCustom{
}
class OppoCustomerSaleCountRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleCountRepositoryCustom{

}

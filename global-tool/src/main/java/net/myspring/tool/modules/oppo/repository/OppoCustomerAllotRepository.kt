package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerAllot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerAllotRepository : BaseRepository<OppoCustomerAllot, String>, OppoCustomerAllotRepositoryCustom {

}
interface OppoCustomerAllotRepositoryCustom{
}
class OppoCustomerAllotRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerAllotRepositoryCustom{

}

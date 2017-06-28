package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerRepository : BaseRepository<OppoCustomer, String>, OppoCustomerRepositoryCustom {

}
interface OppoCustomerRepositoryCustom{
}
class OppoCustomerRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerRepositoryCustom{

}

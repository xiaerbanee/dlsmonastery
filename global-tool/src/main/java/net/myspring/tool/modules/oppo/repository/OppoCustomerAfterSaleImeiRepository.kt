package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomer
import net.myspring.tool.modules.oppo.domain.OppoCustomerAfterSaleImei
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerAfterSaleImeiRepository : BaseRepository<OppoCustomerAfterSaleImei, String>, OppoCustomerAfterSaleImeiRepositoryCustom {

}
interface OppoCustomerAfterSaleImeiRepositoryCustom{
}
class OppoCustomerAfterSaleImeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerAfterSaleImeiRepositoryCustom{

}

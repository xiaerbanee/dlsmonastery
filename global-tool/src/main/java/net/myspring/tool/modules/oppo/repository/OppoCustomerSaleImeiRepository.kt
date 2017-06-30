package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerSaleImei
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerSaleImeiRepository : BaseRepository<OppoCustomerSaleImei, String>, OppoCustomerSaleImeiRepositoryCustom {

}
interface OppoCustomerSaleImeiRepositoryCustom{
}
class OppoCustomerSaleImeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerSaleImeiRepositoryCustom{

}

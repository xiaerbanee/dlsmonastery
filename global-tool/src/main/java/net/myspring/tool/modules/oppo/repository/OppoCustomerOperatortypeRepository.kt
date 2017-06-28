package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerOperatortype
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

interface OppoCustomerOperatortypeRepository : BaseRepository<OppoCustomerOperatortype, String>, OppoCustomerOperatortypeRepositoryCustom {

}
interface OppoCustomerOperatortypeRepositoryCustom{
}
class OppoCustomerOperatortypeRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerOperatortypeRepositoryCustom{

}

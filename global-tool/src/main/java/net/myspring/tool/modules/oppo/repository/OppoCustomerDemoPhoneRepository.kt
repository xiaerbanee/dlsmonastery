package net.myspring.tool.modules.oppo.repository;
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoCustomerDemoPhone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


interface OppoCustomerDemoPhoneRepository : BaseRepository<OppoCustomerDemoPhone, String>, OppoCustomerDemoPhoneRepositoryCustom {

}
interface OppoCustomerDemoPhoneRepositoryCustom{
}
class OppoCustomerDemoPhoneRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : OppoCustomerDemoPhoneRepositoryCustom{

}

package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.springframework.beans.factory.annotation.Autowired

;
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantAgentProductSelRepository : BaseRepository<OppoPlantAgentProductSel, String>, OppoPlantAgentProductSelRepositoryCustom {

    @Query("select  t from #{#entityName}  t where t.itemNumber in (?1)")
    fun findItemNumbers(itemNumbers:MutableList<String>): MutableList<OppoPlantAgentProductSel>

    @Query("select t from #{#entityName}  t  where t.productId is not null or t.lxProductId is not null")
    fun findAllByProductId(): MutableList<OppoPlantAgentProductSel>
}
interface OppoPlantAgentProductSelRepositoryCustom{
    fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel>
}
class OppoPlantAgentProductSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) : OppoPlantAgentProductSelRepositoryCustom{

    override fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel> {
        val paramMap =Maps.newHashMap<String,Any?>()
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("brandId",branchId);
        val simpleJdbcCall=SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantAgentProductSel").returningResultSet("returnValue", BeanPropertyRowMapper(OppoPlantAgentProductSel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantAgentProductSel>;
    }
}

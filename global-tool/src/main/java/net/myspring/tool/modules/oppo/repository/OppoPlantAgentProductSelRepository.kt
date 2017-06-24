package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.springframework.beans.factory.annotation.Autowired

;
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantAgentProductSelRepository : BaseRepository<OppoPlantAgentProductSel, String>, OppoPlantAgentProductSelRepositoryCustom {

}
interface OppoPlantAgentProductSelRepositoryCustom{
    fun findItemNumbers(itemNumbers: MutableList<String>): MutableList<String>
    fun findAllByProductId():MutableList<OppoPlantAgentProductSel>
    fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel>
}
class OppoPlantAgentProductSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) : OppoPlantAgentProductSelRepositoryCustom{

    override fun findItemNumbers(itemNumbers:MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("itemNumbers",itemNumbers);
        val  sql=" select distinct t.item_number from oppo_plant_agent_product_sel t where t.item_number in (:itemNumbers)";
        return namedParameterJdbcTemplate.query(sql,paramMap,BeanPropertyRowMapper(String::class.java));
    }

    override fun findAllByProductId(): MutableList<OppoPlantAgentProductSel> {
        val  sql="select * from oppo_plant_agent_product_sel t  where t.product_id is not null or t.lx_product_id is not null";
        return namedParameterJdbcTemplate.query(sql,BeanPropertyRowMapper(OppoPlantAgentProductSel::class.java));
    }

    override fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel> {
        val paramMap =Maps.newHashMap<String,Any?>()
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("brandId",branchId);
        val simpleJdbcCall=SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantAgentProductSel").returningResultSet("returnValue", BeanPropertyRowMapper(OppoPlantAgentProductSel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantAgentProductSel>;
    }
}

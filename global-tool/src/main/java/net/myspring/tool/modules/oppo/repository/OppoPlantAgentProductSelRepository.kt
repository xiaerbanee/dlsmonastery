package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery
import net.myspring.util.text.StringUtils
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired

;
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantAgentProductSelRepository : BaseRepository<OppoPlantAgentProductSel, String>, OppoPlantAgentProductSelRepositoryCustom {

    @Query("select  t from #{#entityName}  t where t.itemNumber in (?1) and t.companyName = ?2 ")
    fun findItemNumbers(itemNumbers:MutableList<String>,companyName:String): MutableList<OppoPlantAgentProductSel>

    @Query("select t from #{#entityName}  t  where t.productId is not null or t.lxProductId is not null")
    fun findAllByProductId(): MutableList<OppoPlantAgentProductSel>
}
interface OppoPlantAgentProductSelRepositoryCustom{
    fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel>
    fun findAll(oppoPlantAgentProductSelQuery : OppoPlantAgentProductSelQuery):MutableList<OppoPlantAgentProductSelDto>
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

    override fun findAll(oppoPlantAgentProductSelQuery: OppoPlantAgentProductSelQuery): MutableList<OppoPlantAgentProductSelDto> {
        var sb = StringBuilder("""
            SELECT t1.* FROM oppo_plant_agent_product_sel t1 WHERE 1=1
        """)
        if(CollectionUtils.isNotEmpty(oppoPlantAgentProductSelQuery.itemNumberList)){
            sb.append(""" AND item_number IN (:itemNumberList)""")
        }
        if(StringUtils.isNotBlank(oppoPlantAgentProductSelQuery.itemDesc)){
            sb.append(""" AND item_desc LIKE CONCAT('%',:itemDesc,'%')""")
        }
        if(CollectionUtils.isNotEmpty(oppoPlantAgentProductSelQuery.productIdList)){
            sb.append(""" AND (t1.product_id in (:productIdList) OR t1.lx_product_id in (:productIdList))""")
        }
        if (StringUtils.isNotBlank(oppoPlantAgentProductSelQuery.companyName)){
            sb.append(""" AND t1.company_name = :companyName """)
        }
        return namedParameterJdbcTemplate.query(sb.toString(),BeanPropertySqlParameterSource(oppoPlantAgentProductSelQuery),BeanPropertyRowMapper(OppoPlantAgentProductSelDto::class.java));
    }
}

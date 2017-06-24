package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall

import java.time.LocalDate;
;

/**
 * Created by admin on 2016/10/11.
 */
interface OppoPlantProductItemelectronSelRepository : BaseRepository<OppoPlantProductItemelectronSel, String>, OppoPlantProductItemelectronSelRepositoryCustom {

    @Query("select  t from #{#entityName}  t where t.productNo in (?1)")
    fun findProductNos(productNos: MutableList<String>): MutableList<OppoPlantProductItemelectronSel>

}

interface OppoPlantProductItemelectronSelRepositoryCustom{
    fun plantProductItemelectronSel(companyId: String, password: String, systemDate: String): MutableList<OppoPlantProductItemelectronSel>
}

class OppoPlantProductItemelectronSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) :OppoPlantProductItemelectronSelRepositoryCustom{

    override fun plantProductItemelectronSel(companyId: String, password: String, systemDate: String): MutableList<OppoPlantProductItemelectronSel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("dta",systemDate);
        val simpleJdbcCall= SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantProductItemelectronSel").returningResultSet("returnValue",BeanPropertyRowMapper(OppoPlantProductItemelectronSel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantProductItemelectronSel>
    }
}

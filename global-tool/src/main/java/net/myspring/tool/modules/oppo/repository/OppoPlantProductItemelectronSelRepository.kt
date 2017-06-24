package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import org.springframework.beans.factory.annotation.Autowired
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



}

interface OppoPlantProductItemelectronSelRepositoryCustom{
    fun findProductNos(productNos: MutableList<String>): MutableList<String>
    fun plantProductItemelectronSel(companyId: String, password: String, systemDate: String): MutableList<OppoPlantProductItemelectronSel>
}

class OppoPlantProductItemelectronSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) :OppoPlantProductItemelectronSelRepositoryCustom{
    override fun findProductNos(productNos: MutableList<String>): MutableList<String>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("productNos",productNos);
        return namedParameterJdbcTemplate.query("""
              select  t.product_no from oppo_plant_product_itemelectron_sel t
              where t.product_no in (:productNos)
        """,paramMap, BeanPropertyRowMapper(String::class.java));
    }

    override fun plantProductItemelectronSel(companyId: String, password: String, systemDate: String): MutableList<OppoPlantProductItemelectronSel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("dta",systemDate);
        val simpleJdbcCall= SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantProductItemelectronSel").returningResultSet("returnValue",BeanPropertyRowMapper(OppoPlantProductItemelectronSel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantProductItemelectronSel>
    }
}

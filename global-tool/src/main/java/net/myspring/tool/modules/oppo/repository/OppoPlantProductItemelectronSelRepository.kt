package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import java.lang.StringBuilder

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
    fun findSynList(dateStart:String,dateEnd:String,agentCodes:MutableList<String>): MutableList<OppoPlantProductItemelectronSel>
    fun plantProductItemelectronSel(companyId: String, password: String, systemDate: String): MutableList<OppoPlantProductItemelectronSel>
}

class OppoPlantProductItemelectronSelRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) :OppoPlantProductItemelectronSelRepositoryCustom{

    override fun findSynList(dateStart:String, dateEnd:String, agentCodes:MutableList<String>): MutableList<OppoPlantProductItemelectronSel> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
         select
           *
        from
           oppo_plant_product_itemelectron_sel t
        where
          t.created_time>=:dateStart
          and t.created_time<=:dateEnd
          and t.customer_id in (:agentCodes)
            """,paramMap,BeanPropertyRowMapper(OppoPlantProductItemelectronSel::class.java));
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

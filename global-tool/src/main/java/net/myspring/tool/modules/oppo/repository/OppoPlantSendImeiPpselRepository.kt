package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.stereotype.Component

import java.time.LocalDate;

interface OppoPlantSendImeiPpselRepository : BaseRepository<OppoPlantSendImeiPpsel, String>, OppoPlantSendImeiPpselRepositoryCustom {

    @Query("""
    SELECT
        t FROM #{#entityName} t
    WHERE
        t.imei in ?1
        """)
    fun findByimeis(imeis:MutableList<String>):MutableList<OppoPlantSendImeiPpsel>

}
interface OppoPlantSendImeiPpselRepositoryCustom{
    fun findSynList(dateStart:LocalDate,dateEnd:LocalDate,agentCodes:MutableList<String>): MutableList<OppoPlantSendImeiPpsel>
    fun plantSendImeiPPSel(companyId: String,  password: String, createdTime: String): MutableList<OppoPlantSendImeiPpsel>
}


@Component
class OppoPlantSendImeiPpselRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) :OppoPlantSendImeiPpselRepositoryCustom {

    override fun findSynList(dateStart:LocalDate, dateEnd:LocalDate, agentCodes:MutableList<String>): MutableList<OppoPlantSendImeiPpsel> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
            select t.id,t.company_id,t.bill_id,t.imei,t.meid,t.created_time,t.dls_product_id,t.imei_state,t.remarks,t.ime2 from oppo_plant_send_imei_ppsel t
            where t.created_time >= :dateStart
            and t.created_time <= :dateEnd
            and t.company_id in (:agentCodes)
            """,paramMap,BeanPropertyRowMapper(OppoPlantSendImeiPpsel::class.java));
    }

    override fun plantSendImeiPPSel(companyId: String, password: String, createdTime: String): MutableList<OppoPlantSendImeiPpsel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("dta",createdTime);
        val simpleJdbcCall= SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantSendImeiPPSel").returningResultSet("returnValue",BeanPropertyRowMapper(OppoPlantSendImeiPpsel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantSendImeiPpsel>
    }
}

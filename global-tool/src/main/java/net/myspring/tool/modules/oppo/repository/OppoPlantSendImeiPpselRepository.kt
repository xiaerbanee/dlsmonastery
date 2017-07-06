package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpselDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.stereotype.Component

import java.time.LocalDate;

interface OppoPlantSendImeiPpselRepository : BaseRepository<OppoPlantSendImeiPpsel, String>, OppoPlantSendImeiPpselRepositoryCustom {

    @Query("select  t from #{#entityName}  t where t.imei in (?1)")
    fun findByimeis(imeis:MutableList<String>):MutableList<OppoPlantSendImeiPpsel>

}
interface OppoPlantSendImeiPpselRepositoryCustom{
    fun findSynList(dateStart:String,dateEnd:String,agentCodes:MutableList<String>): MutableList<OppoPlantSendImeiPpselDto>
    fun plantSendImeiPPSel(companyId: String,  password: String, dateTime: String): MutableList<OppoPlantSendImeiPpsel>
    fun findListByCreatedDate(dateTimeStart: LocalDate,dateTimeEnd: LocalDate):MutableList<OppoPlantSendImeiPpselDto>
}


@Component
class OppoPlantSendImeiPpselRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,val jdbcTemplate: JdbcTemplate) :OppoPlantSendImeiPpselRepositoryCustom {

    override fun findSynList(dateStart:String, dateEnd:String, agentCodes:MutableList<String>): MutableList<OppoPlantSendImeiPpselDto> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
         select
            t.*, g.color_id as colorId,
            g.product_id as productId,
            g.lx_product_id as lxProductId
        from
            oppo_plant_send_imei_ppsel t,
            oppo_plant_agent_product_sel g
        where
            t.dls_product_id = g.item_number
         and t.created_time>=:dateStart
         and t.created_time<=:dateEnd
        and t.company_id in (:agentCodes)
            """,paramMap,BeanPropertyRowMapper(OppoPlantSendImeiPpselDto::class.java));
    }

    override fun plantSendImeiPPSel(companyId: String, password: String, dateTime: String): MutableList<OppoPlantSendImeiPpsel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("agentId",companyId);
        paramMap.put("pwd",password);
        paramMap.put("dta",dateTime);
        val simpleJdbcCall= SimpleJdbcCall(jdbcTemplate);
        return simpleJdbcCall.withProcedureName("plantSendImeiPPSel").returningResultSet("returnValue",BeanPropertyRowMapper(OppoPlantSendImeiPpsel::class.java)).execute(paramMap).get("returnValue") as MutableList<OppoPlantSendImeiPpsel>
    }

    override fun findListByCreatedDate(dateTimeStart: LocalDate, dateTimeEnd: LocalDate): MutableList<OppoPlantSendImeiPpselDto> {
        val map = Maps.newHashMap<String,LocalDate>();
        map.put("dateTimeStart",dateTimeStart)
        map.put("dateTimeEnd",dateTimeEnd)
        return namedParameterJdbcTemplate.query("""
            SELECT im.*, agent.product_id,agent.lx_product_id
            FROM
                oppo_plant_send_imei_ppsel im,
                oppo_plant_agent_product_sel agent
            WHERE
                im.dls_product_id = agent.item_number
                AND im.created_time >= :dateTimeStart
                AND im.created_time < :dateTimeEnd
            """,map,BeanPropertyRowMapper(OppoPlantSendImeiPpselDto::class.java))
    }
}

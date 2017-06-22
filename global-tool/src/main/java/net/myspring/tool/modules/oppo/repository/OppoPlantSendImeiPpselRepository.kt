package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

import java.time.LocalDate;
;

/**
 * Created by admin on 2016/10/11.
 */
@Component
class OppoPlantSendImeiPpselRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findImeis(imeis:MutableList<String>): MutableList<String> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("imeis",imeis);
        val  sql="select t.imei from #{#entityName} t where t.imei in :imeis";
        return namedParameterJdbcTemplate.query(sql,paramMap, BeanPropertyRowMapper(String::class.java));
    }

    fun findSynList(dateStart:LocalDate,dateEnd:LocalDate,agentCodes:MutableList<String>): MutableList<OppoPlantSendImeiPpsel> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
            select t.id,t.company_id,t.bill_id,t.imei,t.meid,t.created_time,t.dls_product_id,t.imei_state,t.remarks,t.ime2,se.product_id as productId,se.lx_product_id as lxProductId,se.color_id as colorId from oppo_plant_send_imei_ppsel t left join oppo_plant_agent_product_sel sel on t.dls_product_id=se.item_number
            where t.created_time >= :dateStart
            and t.created_time <= :dateEnd
            and t.company_id in :agentCodes
            """,paramMap,BeanPropertyRowMapper(OppoPlantSendImeiPpsel::class.java));
    }
}

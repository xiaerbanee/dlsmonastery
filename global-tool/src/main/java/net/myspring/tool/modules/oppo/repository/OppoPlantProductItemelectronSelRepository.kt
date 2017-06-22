package net.myspring.tool.modules.oppo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
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
class OppoPlantProductItemelectronSelRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {
    fun findProductNos(productNos: MutableList<String>): MutableList<String>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("productNos",productNos);
        return namedParameterJdbcTemplate.query("""
              select t.product_no from oppo_plant_product_itemelectron_sel t2
              where t.product_no in :productNos
        """,paramMap, BeanPropertyRowMapper(String::class.java));
    }

//    fun updateItemNumber(dateStart: LocalDate,dateEnd:LocalDate,companyId:String):Int{
//        val paramMap = Maps.newHashMap<String, Any>();
//        paramMap.put("dateStart",dateStart);
//        paramMap.put("dateEnd",dateEnd);
//        paramMap.put("companyId",companyId);
//        return namedParameterJdbcTemplate.update("""
//            update crm_product_ime t1,oppo_plant_product_itemelectron_sel t2
//            set t1.retail_date=t2.date_time
//            where t1.ime=t2.product_no
//            and t2.date_time >= :dateStart
//            and t2.date_time <= :dateEnd
//            and t1.company_id=companyId
//        """,paramMap);
//    }
}

package net.myspring.tool.modules.oppo.repository;
import com.google.common.collect.Maps
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

import java.time.LocalDate;

/**
 * Created by admin on 2016/10/29.
 */
@Component
class OppoRepository @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate){

    fun plantProductSel(companyId: String,password: String, branchId: String): MutableList<OppoPlantProductSel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("companyId",companyId);
        paramMap.put("password",password);
        paramMap.put("branchId",branchId);
        return namedParameterJdbcTemplate.query("""
              call plantProductSel (
              :companyId,
              :password,
              :branchId)
        """,paramMap,BeanPropertyRowMapper(OppoPlantProductSel::class.java));
    }

    fun plantAgentProductSel(companyId: String, password: String, branchId: String): MutableList<OppoPlantAgentProductSel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("companyId",companyId);
        paramMap.put("password",password);
        paramMap.put("branchId",branchId);
        return namedParameterJdbcTemplate.query("""
            call PlantAgentProductSel (
            :companyId,
            :password,
            :branchId)
        """,paramMap,BeanPropertyRowMapper(OppoPlantAgentProductSel::class.java));
    }

    fun plantSendImeiPPSel(companyId: String,  password: String, createdTime: LocalDate): MutableList<OppoPlantSendImeiPpsel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("companyId",companyId);
        paramMap.put("password",password);
        paramMap.put("createdTime",createdTime);
        return namedParameterJdbcTemplate.query("""
            call PlantSendImeiPPSel (
            :companyId,
            :password,
            :createdTime
        )
        """,paramMap,BeanPropertyRowMapper(OppoPlantSendImeiPpsel::class.java));
    }

    fun plantProductItemelectronSel(companyId: String, password: String, systemDate: LocalDate): MutableList<OppoPlantProductItemelectronSel>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("companyId",companyId);
        paramMap.put("password",password);
        paramMap.put("systemDate",systemDate);
        return namedParameterJdbcTemplate.query("""
          call PlantProductItemelectronSel (
         :companyId,
         :password,
         :systemDate
        )
        """,paramMap,BeanPropertyRowMapper(OppoPlantProductItemelectronSel::class.java));
    }
}

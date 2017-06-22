package net.myspring.tool.modules.imoo.repository

import com.google.common.collect.Maps
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Component
class ImooRepository @Autowired constructor(val namedParameterJdbcTemplate:NamedParameterJdbcTemplate){

    fun plantBasicProducts(): MutableList<ImooPlantBasicProduct> {
        return namedParameterJdbcTemplate.query("""
            select *
            from
            PlantBasicProduct
            """,BeanPropertyRowMapper(ImooPlantBasicProduct::class.java));
    }

        fun plantPrdocutImeiDeliverByDate(dateStart: LocalDate,  dateEnd: LocalDate, agentCodes: MutableList<String>): MutableList<ImooPrdocutImeiDeliver>{
            val paramMap = Maps.newHashMap<String, Any>();
            paramMap.put("dateStart",dateStart);
            paramMap.put("dateEnd",dateEnd);
            paramMap.put("agentCodes",agentCodes);
            return namedParameterJdbcTemplate.query("""
                select * from prdocutimeideliver t
                where t.creation_date >= :dateStart
                and t.creation_date < :dateEnd
                and mainagentid in :agentCodes
            """,paramMap,BeanPropertyRowMapper(ImooPrdocutImeiDeliver::class.java));
        }
}
package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import java.time.LocalDate;

interface VivoPlantSendimeiRepository : BaseRepository<VivoPlantSendimei, String>, VivoPlantSendimeiRepositoryCustom {

    @Query("""
        select t from #{#entityName} t
        where t.createdTime >=:dateStart
        and t.createdTime <= :dateEnd
        and t.companyId in (:agentCodes)
        """)
    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>

    @Query("select t from #{#entityName} t where t.imei in (:imeiList)")
    fun findImeis(@Param("imeiList") imeiList: MutableList<String>): MutableSet<VivoPlantSendimei>
}
interface VivoPlantSendimeiRepositoryCustom{
    fun findErrorItemNumbers(@Param("dateStart") dateStart: String, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<String>

}
class VivoPlantSendimeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantSendimeiRepositoryCustom {
    override fun findErrorItemNumbers(@Param("dateStart") dateStart: String, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<String>{
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart", dateStart);
        paramMap.put("agentCodes", agentCodes);

        return namedParameterJdbcTemplate.query("""
            select distinct t.product_id
            from vivo_plant_sendimei t
            where t.created_time> :dateStart
            and t.company_id in (:agentCodes)
            """,paramMap, BeanPropertyRowMapper(String::class.java));
    }
}
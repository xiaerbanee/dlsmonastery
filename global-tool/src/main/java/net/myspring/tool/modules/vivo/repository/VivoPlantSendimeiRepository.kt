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

}
interface VivoPlantSendimeiRepositoryCustom{
    fun findPlantSendimei(dateStart: String, dateEnd: String, agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>
    fun findImeis( imeiList: MutableList<String>): MutableList<VivoPlantSendimei>
}

class VivoPlantSendimeiRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantSendimeiRepositoryCustom {
    override fun findPlantSendimei(dateStart: String, dateEnd: String, agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>{
        var paramMap=Maps.newHashMap<String,Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);
        return namedParameterJdbcTemplate.query("""
      select *  from vr_plant_sendimei_m13e00 t1
        where t1.createdtime >= :dateStart
        and t1.createdtime < :dateEnd
        and t1.company_id in :agentCodes
      """,paramMap,BeanPropertyRowMapper(VivoPlantSendimei::class.java));
    }

    override fun findImeis(imeiList:MutableList<String>): MutableList<VivoPlantSendimei>{
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("imeiList",imeiList);
        return namedParameterJdbcTemplate.query("""
                select t  from vivo_plant_sendimei t where t.imei in(:imeiList)
        """,  paramMap,BeanPropertyRowMapper(VivoPlantSendimei::class.java));
    }

}
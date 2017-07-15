package net.myspring.tool.modules.vivo.repository;

import com.google.common.collect.Maps
import net.myspring.tool.common.repository.BaseRepository
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import java.time.LocalDate;


interface VivoPlantElectronicsnRepository : BaseRepository<VivoPlantElectronicsn, String>, VivoPlantElectronicsnRepositoryCustom {
    @Query("select  t from #{#entityName}  t where t.snImei in (?1)")
    fun findSnImeis(snImeis: MutableList<String>): MutableList<VivoPlantElectronicsn>
}
interface VivoPlantElectronicsnRepositoryCustom{
    fun findSynList(dateStart:String,dateEnd:String,agentCodes:MutableList<String>): MutableList<VivoPlantElectronicsn>
    fun findPlantElectronicsn(dateStart: String, dateEnd: String): MutableList<VivoPlantElectronicsn>
    }

class VivoPlantElectronicsnRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) :VivoPlantElectronicsnRepositoryCustom {

    override fun findSynList(dateStart:String, dateEnd:String, agentCodes:MutableList<String>): MutableList<VivoPlantElectronicsn> {
        val paramMap = Maps.newHashMap<String, Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        paramMap.put("agentCodes",agentCodes);

        return namedParameterJdbcTemplate.query("""
         select
           *
        from
           vivo_plant_electronicsn t
        where
          t.create_time>=:dateStart
          and t.create_time<:dateEnd
          and t.company_id in (:agentCodes)
            """,paramMap,BeanPropertyRowMapper(VivoPlantElectronicsn::class.java));
    }

    override fun findPlantElectronicsn(dateStart: String, dateEnd: String): MutableList<VivoPlantElectronicsn>{
        var paramMap=Maps.newHashMap<String,Any>();
        paramMap.put("dateStart",dateStart);
        paramMap.put("dateEnd",dateEnd);
        return namedParameterJdbcTemplate.query("""
      select *
        from vr_plant_electronicsn_m13e00 t
        where t.createtime>=:dateStart
          and t.createtime<:dateEnd
      """,paramMap,BeanPropertyRowMapper(VivoPlantElectronicsn::class.java));
    }
}
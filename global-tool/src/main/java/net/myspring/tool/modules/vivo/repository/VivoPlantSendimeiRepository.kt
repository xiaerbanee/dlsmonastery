package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2016/10/17.
 */
interface VivoPlantSendimeiRepository {

    fun findSynList(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: List<String>, @Param("companyId") companyId: Long?): List<VivoPlantSendimei>

    fun findImeis(@Param("imeiList") imeiList: List<String>): Set<String>

    fun findErrorItemNumbers(@Param("dateStart") dateStart: LocalDate, @Param("agentCodes") agentCodes: List<String>, @Param("companyId") companyId: Long?): List<String>

    fun save(vivoPlantSendimeis: List<VivoPlantSendimei>): Int
}

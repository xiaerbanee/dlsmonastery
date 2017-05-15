package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2016/10/17.
 */
@Mapper
public interface VivoPlantSendimeiMapper {

    List<VivoPlantSendimei> findSynList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("agentCodes") List<String> agentCodes, @Param("companyId") Long companyId);

    Set<String> findImeis(@Param("imeiList") List<String> imeiList);

    List<String> findErrorItemNumbers(@Param("dateStart") LocalDate dateStart, @Param("agentCodes") List<String> agentCodes, @Param("companyId") Long companyId);

    int save(List<VivoPlantSendimei> vivoPlantSendimeis);
}

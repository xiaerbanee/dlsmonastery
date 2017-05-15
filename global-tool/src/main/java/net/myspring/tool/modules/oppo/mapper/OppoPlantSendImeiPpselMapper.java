package net.myspring.tool.modules.oppo.mapper;

import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@Mapper
public interface OppoPlantSendImeiPpselMapper {

    int save(List<OppoPlantSendImeiPpsel> list);

    List<String> findImeis(List<String> imeis);

    List<OppoPlantSendImeiPpsel> findSynList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("agentCodes") List<String> agentCodes);

}

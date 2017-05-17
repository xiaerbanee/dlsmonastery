package net.myspring.tool.modules.imoo.mapper;
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ImooMapper {

    List<ImooPlantBasicProduct> plantBasicProducts();

    List<ImooPrdocutImeiDeliver> plantPrdocutImeiDeliverByDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("agentCodes") List<String> agentCodes);

}

package net.myspring.tool.modules.oppo.mapper;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/29.
 */
@Mapper
public interface OppoMapper {

    List<OppoPlantProductSel> plantProductSel(@Param("companyId") String companyId, @Param("password") String password, @Param("branchId") String branchId);

    List<OppoPlantAgentProductSel> plantAgentProductSel(@Param("companyId") String companyId, @Param("password") String password, @Param("branchId") String branchId);

    List<OppoPlantSendImeiPpsel> plantSendImeiPPSel(@Param("companyId") String companyId, @Param("password") String password, @Param("createdTime") LocalDate createdTime);

    List<OppoPlantProductItemelectronSel> plantProductItemelectronSel(@Param("companyId") String companyId, @Param("password") String password, @Param("systemDate") LocalDate systemDate);
}

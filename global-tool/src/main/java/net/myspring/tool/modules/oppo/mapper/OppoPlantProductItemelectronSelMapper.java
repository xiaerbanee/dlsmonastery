package net.myspring.tool.modules.oppo.mapper;

import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@Mapper
public interface OppoPlantProductItemelectronSelMapper {

    int save(List<OppoPlantProductItemelectronSel> list);

    List<String> findProductNos(List<String> productNos);

    int updateItemNumber();

    int updateProductImeRetailDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

}

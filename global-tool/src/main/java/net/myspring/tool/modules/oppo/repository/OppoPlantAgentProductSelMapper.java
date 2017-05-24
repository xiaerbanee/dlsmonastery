package net.myspring.tool.modules.oppo.mapper;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@Mapper
public interface OppoPlantAgentProductSelMapper {

    OppoPlantAgentProductSel findOne(Long id);

    List<OppoPlantAgentProductSel> findAll();

    int save(List<OppoPlantAgentProductSel> list);

    List<String> findItemNumbers(List<String> itemNumbers);

}

package net.myspring.tool.modules.oppo.mapper;

import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@Mapper
public interface OppoPlantProductSelMapper {

    int save(List<OppoPlantProductSel> list);

    List<String> findColorIds(List<String> colorIds);

}

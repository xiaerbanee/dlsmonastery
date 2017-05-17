package net.myspring.tool.modules.imoo.mapper;

import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImooPlantBasicProductMapper {

    List<String> findSegment1s(List<String> segment1s);

    int save(List<ImooPlantBasicProduct> list);

    ImooPlantBasicProduct findById(String id);

}

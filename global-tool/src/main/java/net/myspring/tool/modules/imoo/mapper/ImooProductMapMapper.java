package net.myspring.tool.modules.imoo.mapper;

import net.myspring.tool.modules.imoo.domain.ImooProductMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImooProductMapMapper {

    List<ImooProductMap> findAll();

}

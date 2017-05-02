package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.PositionModule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositionModuleMapper extends MyMapper<PositionModule,String>{

    List<PositionModule> findByPositionId(String positionId);
}

package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.PositionBackend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositionBackendMapper extends MyMapper<PositionBackend,String>{

    List<PositionBackend> findByPositionId(String positionId);
}

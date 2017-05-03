package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.RoleModule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleModuleMapper extends MyMapper<RoleModule,String>{

    List<RoleModule> findByPositionId(String positionId);

    int deleteByRoleId(String roleId);
}

package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.INTERNAL;

/**
 * Created by wangzm on 2017/5/2.
 */
@Mapper
public interface RolePermissionMapper extends MyMapper<RolePermission,String> {

    int deleteByRoleId(String roleId);

    int deleteByPermissionId(String permissionId);
}

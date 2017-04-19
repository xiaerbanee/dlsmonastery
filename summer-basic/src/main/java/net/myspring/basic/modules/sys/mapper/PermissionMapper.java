package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionMapper extends MyMapper<Permission,String> {

    List<Permission> findByPositionId(String positionId);

    List<Permission> findByMenuId(String menuId);

    List<Permission> findByMenuIds(List<String> menuIds);

    Permission findByPermission(String permissionStr);

    List<Permission> findByPermissionLike(String permissionStr);

    Page<PermissionDto> findPage(Pageable pageable, @Param("p")PermissionQuery permissionQuery);

    int savePermissionPosition(@Param("permissionId")String permissionId,@Param("positionIds")List<String> positionIds);

    int deletePermissionPosition(@Param("permissionId")String permissionId);

    List<NameValueDto> findNameValueByPositionId(List<String> permissionIds);

}

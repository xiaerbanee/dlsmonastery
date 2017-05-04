package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface PermissionMapper extends MyMapper<Permission,String> {

    List<Permission> findByRoleId(String roleId);

    List<Permission> findByRoleAndAccount(@Param("roleId")String roleId,@Param("accountId")String accountId);

    List<Permission> findByMenuId(String menuId);

    List<Permission> findByMenuIds(List<String> menuIds);

    Permission findByPermission(String permissionStr);

    List<Permission> findByPermissionLike(String permissionStr);

    Page<PermissionDto> findPage(Pageable pageable, @Param("p")PermissionQuery permissionQuery);
}

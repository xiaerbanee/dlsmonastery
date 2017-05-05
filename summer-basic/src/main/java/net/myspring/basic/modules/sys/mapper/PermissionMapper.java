package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "permissions")
public interface PermissionMapper extends BaseMapper<Permission,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Permission findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Permission> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Permission permission);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Permission permission);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Permission> findAllEnabled();

    @UpdateProvider(type=MyProvider.class,method = MyProvider.LOGIC_DELETE_BY_IDS)
    int logicDeleteByIds(List<String> ids);

    List<Permission> findByRoleId(String roleId);

    List<Permission> findByRoleAndAccount(@Param("roleId")String roleId,@Param("accountId")String accountId);

    List<Permission> findByMenuId(String menuId);

    List<Permission> findByMenuIds(List<String> menuIds);

    Permission findByPermission(String permissionStr);

    List<Permission> findByPermissionLike(String permissionStr);

    Page<PermissionDto> findPage(Pageable pageable, @Param("p")PermissionQuery permissionQuery);
}

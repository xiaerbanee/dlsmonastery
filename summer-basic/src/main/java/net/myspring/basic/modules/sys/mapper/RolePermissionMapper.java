package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Mapper
@CacheDefaults(cacheName = "rolePermissions")
public interface RolePermissionMapper extends BaseMapper<RolePermission,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    RolePermission findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<RolePermission> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue RolePermission rolePermission);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue RolePermission rolePermission);

    @InsertProvider(type = MyProvider.class, method = MyProvider.BATCH_SAVE)
    @Options(useGeneratedKeys = true)
    int batchSave(List<RolePermission> rolePermissionList);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<RolePermission> findAllEnabled();

    List<RolePermission> findByRoleId(String roleId);

    List<RolePermission> findAllByRoleId(String roleId);

    int setEnabledByRoleId(@Param("enabled")boolean enabled,@Param("roleId") String roleId);

    int setEnabledByPermissionIdList(@Param("enabled")boolean enabled,@Param("list") List<String> permissionIdList);

    List<RolePermission> findAllByPermissionId(String permissionId);

    int setEnabledByPermissionId(@Param("enabled")boolean enabled,@Param("permissionId") String permissionId);

    int setEnabledByRoleIdList(@Param("enabled")boolean enabled,@Param("list") List<String> roleIdList);
}

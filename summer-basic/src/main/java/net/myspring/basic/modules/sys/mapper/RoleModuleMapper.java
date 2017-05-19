package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "roleModules")
public interface RoleModuleMapper extends BaseMapper<RoleModule,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    RoleModule findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue RoleModule roleModule);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue RoleModule roleModule);

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @InsertProvider(type = MyProvider.class, method = MyProvider.BATCH_SAVE)
    @Options(useGeneratedKeys = true)
    int batchSave(List<RoleModule> roleModuleList);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<RoleModule> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<RoleModule> findAll();

    List<RoleModule> findByRoleId(String roleId);

    List<RoleModule> findAllByRoleId(String roleId);

    int setEnabledByRoleId(@Param("enabled")boolean enabled,@Param("roleId") String roleId);

    int setEnabledByModuleIdList(@Param("enabled")boolean enabled,@Param("list") List<String> moduleIds);
}

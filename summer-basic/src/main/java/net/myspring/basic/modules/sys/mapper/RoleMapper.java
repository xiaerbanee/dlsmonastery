package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Mapper
@CacheDefaults(cacheName = "roles")
public interface RoleMapper extends BaseMapper<Role,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Role findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Role> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Role role);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Role role);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Role> findAllEnabled();

    Page<RoleDto> findPage(Pageable pageable, @Param("p")RoleQuery roleQuery);

    List<Role> findByNameLike(String name);
}

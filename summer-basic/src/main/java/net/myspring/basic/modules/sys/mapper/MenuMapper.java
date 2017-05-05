package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
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
@CacheDefaults(cacheName = "menus")
public interface MenuMapper extends BaseMapper<Menu,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Menu findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Menu> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Menu menu);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Menu menu);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Menu> findAllEnabled();

    Page<MenuDto> findPage(Pageable pageable, @Param("p") MenuQuery menuQuery);

    List<Menu> findByPermissionIsEmpty();

    List<Menu> findByPermissionIsNotEmpty();

    List<Menu> findByMenuCategoryId( String menuCategoryId);

    List<Menu> findByMenuIdsAndMobile(@Param("list")List<String> menuIds,@Param("isMobile")boolean isMobile);
}

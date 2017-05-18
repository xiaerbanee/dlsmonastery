package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.web.query.DictMapQuery;
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

@Mapper
@CacheDefaults(cacheName = "dictMaps")
public interface DictMapMapper extends BaseMapper<DictMap,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    DictMap findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue DictMap dictMap);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue DictMap dictMap);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<DictMap> findAll();

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<DictMap> findAllEnabled();

    List<String> findDistinctCategory();

    Page<DictMapDto> findPage(Pageable pageable, @Param("p")DictMapQuery dictMapQuery);

    List<DictMap> findByCategory(String category);

    DictMapDto findByName(String name);


}

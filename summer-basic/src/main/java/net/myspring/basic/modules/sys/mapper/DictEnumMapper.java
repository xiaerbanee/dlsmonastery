package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
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
@CacheDefaults(cacheName = "dictEnums")
public interface DictEnumMapper extends BaseMapper<DictEnum,String>{

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    DictEnum findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<DictEnum> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue DictEnum dictEnum);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue DictEnum dictEnum);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<DictEnum> findAllEnabled();

    @CacheResult
    List<DictEnum> findByCategory(String category);

    Page<DictEnumDto> findPage(Pageable pageable, @Param("p")DictEnumQuery dictEnumQuery);

    List<String> findDistinctCategory();

    DictEnumDto findByValue(String value);

}

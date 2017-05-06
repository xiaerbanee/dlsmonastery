package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyMapper;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.time.LocalDate;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "demoPhoneTypes")
public interface DemoPhoneTypeMapper extends BaseMapper<DemoPhoneType,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    DemoPhoneType findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<DemoPhoneType> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue DemoPhoneType demoPhoneType);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue DemoPhoneType demoPhoneType);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<DemoPhoneType> findAllEnabled();

    List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate);

    Page<DemoPhoneTypeDto> findPage(Pageable pageable, @Param("p")DemoPhoneTypeQuery demoPhoneTypeQuery);

    List<DemoPhoneType> findLabels(List<String> ids);

}

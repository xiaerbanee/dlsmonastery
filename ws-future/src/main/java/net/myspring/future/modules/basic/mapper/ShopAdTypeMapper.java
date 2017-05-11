package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.future.modules.basic.web.query.ShopAdTypeQuery;
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
@CacheDefaults(cacheName = "shopAdTypes")
public interface ShopAdTypeMapper extends BaseMapper<ShopAdType,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    ShopAdType findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<ShopAdType> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue ShopAdType shopAdType);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue ShopAdType shopAdType);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<ShopAdType> findAllEnabled();

    List<ShopAdTypeDto> findAllByEnabled();

    Page<ShopAdTypeDto> findPage(Pageable pageable, @Param("p")ShopAdTypeQuery shopAdTypeQuery);

    List<ShopAdType> findLabels(List<String> ids);

}

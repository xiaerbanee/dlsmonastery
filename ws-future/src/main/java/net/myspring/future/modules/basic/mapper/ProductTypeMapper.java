package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;
import java.util.Map;

@Mapper
@CacheDefaults(cacheName = "productTypes")
public interface ProductTypeMapper extends BaseMapper<ProductType,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    ProductType findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<ProductType> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue ProductType productType);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue ProductType productType);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<ProductType> findAllEnabled();

    Page<ProductTypeDto> findPage(Pageable pageable, @Param("p") ProductTypeQuery productTypeQuery);

    List<ProductType> findByFilter(@Param("p") Map<String, Object> map);

    List<ProductType> findByDemoPhoneTypeIds(List<String> dempProductTypeIds);

    List<ProductType> findByNameLike(@Param("name") String name);

    List<ProductType> findByIds(List<String> ids);

    int updateDemoPhoneType(@Param("demoPhoneTypeId") String demoPhoneTypeId, @Param("list") List<String> ids);

    int updateDemoPhoneTypeToNull(String demoPhoneTypeId);

    List<ProductType> findAllScoreType();

    List<ProductType> findByDemoPhoneTypeId(String demoPhoneTypeId);

}

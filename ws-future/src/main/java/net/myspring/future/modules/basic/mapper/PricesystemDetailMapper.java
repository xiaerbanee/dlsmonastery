package net.myspring.future.modules.basic.mapper;


import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyMapper;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "pricesystemDetails")
public interface PricesystemDetailMapper extends BaseMapper<PricesystemDetail,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    PricesystemDetail findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<PricesystemDetail> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue PricesystemDetail pricesystemDetail);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue PricesystemDetail pricesystemDetail);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<PricesystemDetail> findAllEnabled();


    List<PricesystemDetail> findByPricesystemIds(List<String> pricesystemIds);

    PricesystemDetail findByPricesystemIdAndProductId(@Param("pricesystemId") String pricesystemId, @Param("productId") String productId);

    List<PricesystemDetail> findByProductIdList(List<String> productIdList);

    List<PricesystemDetail> findByPricesystemId(String pricesystemId);
}

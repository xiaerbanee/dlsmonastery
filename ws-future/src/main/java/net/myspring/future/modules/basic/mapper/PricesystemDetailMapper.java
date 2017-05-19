package net.myspring.future.modules.basic.mapper;


import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

@Mapper
public interface PricesystemDetailMapper extends MyMapper<PricesystemDetail,String> {

    List<PricesystemDetail> findByPricesystemIds(List<String> pricesystemIds);

    PricesystemDetail findByPricesystemIdAndProductId(@Param("pricesystemId") String pricesystemId, @Param("productId") String productId);

    List<PricesystemDetail> findByProductIdList(List<String> productIdList);

    List<PricesystemDetail> findByPricesystemId(String pricesystemId);

    List<PricesystemDetail> findByDepotId(String depotId);
}

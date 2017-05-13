package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */

@Mapper
@CacheDefaults(cacheName = "depotShops")
public interface DepotShopMapper extends BaseMapper<DepotShop,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    DepotShop findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<DepotShop> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue DepotShop depotShop);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue DepotShop depotShop);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<DepotShop> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_BY_IDS)
    List<DepotShop> findByIds(List<String> ids);

    List<DepotDto> findAll(DepotShopQuery depotShopQuery);

    List<DepotDto> findByLikeName(@Param("name")String name,@Param("category")String category);

}
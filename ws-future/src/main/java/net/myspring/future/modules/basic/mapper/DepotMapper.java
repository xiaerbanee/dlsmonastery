package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.web.query.DepotAccountQuery;
import net.myspring.future.modules.basic.web.query.DepotQuery;
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
@CacheDefaults(cacheName = "depots")
public interface DepotMapper extends BaseMapper<Depot,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Depot findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Depot> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Depot depot);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Depot depot);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Depot> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_BY_IDS)
    List<Depot> findByIds(List<String> ids);

    Page<DepotDto> findPage(Pageable pageable, @Param("p")DepotQuery depotQuery);

    List<DepotDto> findShopList(DepotQuery depotShopQuery);

    List<DepotDto> findStoreList(DepotQuery depotShopQuery);

    List<Depot> findByChainId(String chainId);

    List<Depot> findByAccountId(String accountId);

    List<Depot> findByNameList(List<String> nameList);

    Depot findByName(String name);

    DepotDto findShopByGoodsOrderId(String goodsOrderId);

    DepotDto findStoreByGoodsOrderId(String goodsOrderId);

    Page<DepotAccountDto> findDepotAccountList(Pageable pageable, DepotAccountQuery depotAccountQuery);
}

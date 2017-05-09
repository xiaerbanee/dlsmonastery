package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.time.LocalDateTime;
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

    Page<DepotDto> findDepotAccountPage(Pageable pageable, @Param("p") DepotQuery depotQuery);

    List<DepotDto> findShopAccountExportPage(@Param("p") DepotQuery depotQuery);

    List<Depot> findByTypes(List<Integer> depotTypes);

    List<Depot> findByOfficeId(String officeId);

    List<Depot> findByAccountId(String accountId);

    List<Depot> findByFilterAll(@Param("p") DepotQuery depotQuery);

    List<Depot> findByFilter(@Param("p")DepotQuery depotQuery);

    List<String> findChainIds(@Param("p") DepotQuery depotQuery);

    List<Depot> findByChainId(String chainId);

    List<Depot> findAllByOffice(@Param("officeIds") List<String> officeIds);

    List<Depot> findLabels(List<String> ids);

    List<Depot> findByAdPricesystemId(String adPricesystemId);

    Depot findByName(String name);

    List<Depot> findByOutGroupId(String outGroupId);

    List<Depot> findByOutTypeAndOutId(@Param("outType") String outType, @Param("outId") String outId);

    Depot findAllByNameAndOutId(@Param("name") String name, @Param("outId") String outId);

    int deleteDepotAccount(@Param("depotId") String depotId);

    int saveDepotAccount(@Param("depotId") String accountId, @Param("accountIds") List<String> accountIds);

    LocalDateTime getMaxOutDate(@Param("outType") String outType);

}

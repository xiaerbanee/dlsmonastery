package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyMapper;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.dto.DealerDto;
import net.myspring.future.modules.basic.web.query.DealerQuery;
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
@CacheDefaults(cacheName = "dealers")
public interface DealerMapper extends BaseMapper<Dealer,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Dealer findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Dealer> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Dealer dealer);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Dealer dealer);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Dealer> findAllEnabled();
    
    Page<DealerDto> findPage(Pageable pageable, @Param("p")DealerQuery dealerQuery);
}

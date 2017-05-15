package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.mybatis.mapper.BaseMapper;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */

@Mapper
public interface DepotShopMapper extends MyMapper<DepotShop,String> {

    Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotShopQuery);

}
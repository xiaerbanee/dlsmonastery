package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.future.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.domain.DepotShopAttribute;
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
public interface DepotShopAttributeMapper extends CrudMapper<DepotShopAttribute,String> {

}
package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.domain.Company;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by wangzm on 2017/4/12.
 */
@Mapper
@CacheDefaults(cacheName = "companys")
public interface CompanyMapper extends BaseMapper<Company,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Company findOne(String id);

    @javax.cache.annotation.CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Company company);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Company company);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Company> findAll();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Company> findAllEnabled();

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

}

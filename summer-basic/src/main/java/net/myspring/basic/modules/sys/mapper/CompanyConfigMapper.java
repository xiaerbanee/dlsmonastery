package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.domain.CompanyConfig;
import net.myspring.basic.modules.sys.dto.CompanyConfigDto;
import net.myspring.basic.modules.sys.web.query.CompanyConfigQuery;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by zhucc on 2017/4/17.
 */
@Mapper
@CacheDefaults(cacheName = "companyConfigs")
public interface CompanyConfigMapper extends BaseMapper<CompanyConfig,String> {

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue CompanyConfig companyConfig);

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    CompanyConfig findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue CompanyConfig companyConfig);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<CompanyConfig> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<CompanyConfig> findAll();

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    Page<CompanyConfigDto> findPage(Pageable pageable, @Param("p")CompanyConfigQuery companyConfigQuery);

    CompanyConfig findByCode(String code);
}

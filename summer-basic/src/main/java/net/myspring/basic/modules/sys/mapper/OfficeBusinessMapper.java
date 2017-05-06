package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

/**
 * Created by wangzm on 2017/4/24.
 */
@Mapper
@CacheDefaults(cacheName = "businessOffices")
public interface OfficeBusinessMapper extends BaseMapper<OfficeBusiness,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    OfficeBusiness findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<OfficeBusiness> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue OfficeBusiness businessOffice);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue OfficeBusiness businessOffice);

    @InsertProvider(type = MyProvider.class, method = MyProvider.BATCH_SAVE)
    @Options(useGeneratedKeys = true)
    int batchSave(List<OfficeBusiness> officeBusinessList);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<OfficeBusiness> findAllEnabled();

    List<String> findBusinessIdById(String id);

    int removeByBusinessOfficeIds(List<String> businessOfficeIds);

    int removeByOfficeId(String officeId);
}

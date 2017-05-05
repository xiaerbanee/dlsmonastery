package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
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
import java.util.Map;

@Mapper
@CacheDefaults(cacheName = "offices")
public interface OfficeMapper extends BaseMapper<Office,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Office findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Office> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Office office);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Office office);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Office> findAllEnabled();

    List<Office> findByOfficeRuleName(String officeRuleName);

    Office findByAccountId(String accountId);

    List<Office> findByAccountIds(List<String> accountIds);

    List<Office> findByParentIdsLike(String parentId);

    List<Office> findByFilter(@Param("p") Map<String, Object> map);

    List<Office> findByFilterAll(@Param("p") Map<String, Object> map);

    List<Office> findLabels(List<String> ids);

    List<Office> findByAreaIds(List<String> areaIds);

    Page<OfficeDto> findPage(@Param("pageable") Pageable pageable, @Param("p")OfficeQuery officeQuery);

    Office findByOfficeIdAndRuleId(@Param("officeId") String officeId,@Param("officeRuleId") String officeRuleId);

}

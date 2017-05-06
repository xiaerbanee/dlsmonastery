package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
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
 * Created by wangzm on 2017/4/22.
 */
@Mapper
@CacheDefaults(cacheName = "officeRules")
public interface OfficeRuleMapper extends BaseMapper<OfficeRule,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    OfficeRule findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<OfficeRule> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue OfficeRule officeRule);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue OfficeRule officeRule);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<OfficeRule> findAllEnabled();

    Page<OfficeRuleDto> findPage(Pageable pageable, @Param("p")OfficeRuleQuery officeRuleQuery);

    OfficeRule findByName(String name);

    List<OfficeRule> findByType(String type);

    OfficeRule findMaxLevel();

    OfficeRule findNextOfficeRule(String id);
}

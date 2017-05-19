package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.common.mybatis.MyProvider;
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

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
@CacheDefaults(cacheName = "accounts")
public interface AccountMapper extends BaseMapper<Account,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Account findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Account account);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Account account);

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Account> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Account> findAll();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_BY_IDS)
    List<Account> findByIds(Iterable<String> var1);

    Account findByLoginName(String loginName);

    List<Account> findByLoginNameLikeAndType(@Param("p")Map<String,Object> map);

    String findAccountPassword(String id);

    List<Account> findByOfficeIds(List<String> officeIds);

    List<Account> findByPosition(String positionId);

    Page<AccountDto> findPage(Pageable pageable, @Param("p")AccountQuery accountQuery);

    List<Account> findByFilter(@Param("p")AccountQuery accountQuery);

    List<Account> findLabels(List<String> ids);

    List<Account> findByLoginNameList(List<String> loginNames);

    List<Account> findById(String id);
}

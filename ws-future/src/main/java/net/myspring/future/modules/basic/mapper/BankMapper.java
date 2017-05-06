package net.myspring.future.modules.basic.mapper;

import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.common.dto.NameValueDto;
import net.myspring.common.mybatis.MyMapper;
import net.myspring.common.mybatis.MyProvider;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.basic.web.query.BankQuery;
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
@CacheDefaults(cacheName = "banks")
public interface BankMapper extends MyMapper<Bank,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Bank findOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Bank> findAll();

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method =MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Bank bank);

    @UpdateProvider(type=MyProvider.class,method =MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Bank bank);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Bank> findAllEnabled();

    Bank findByName(String name);

    Bank findByOutId(String outId);

    Page<BankDto> findPage(Pageable pageable, @Param("p")BankQuery bankQuery);

    List<NameValueDto> findByBankId(List<String> bankId);

    int deleteBankAccount(@Param("bankId") String bankId);

    int saveAccount(@Param("bankId") String bankId, @Param("accountIds") List<String> accountIds);

    List<Bank> findLabels(List<String> ids);

    List<Bank> findByAccountId(@Param("accountId") String accountId);

    LocalDateTime getMaxOutDate();
}

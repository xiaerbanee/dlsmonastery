package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.common.mybatis.MyProvider;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.mybatis.provider.CrudProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountMapper {

    @Cacheable(value = "accounts",key="#p0")
    @SelectProvider(type=MyProvider.class, method = MyProvider.FIND_ONE)
    Account findOne(String id);

    @CacheEvict(value = "accounts",key="#p0.id")
    @InsertProvider(type=CrudProvider.class, method = MyProvider.SAVE)
    int save(Account account);

    @CacheEvict(value = "accounts",key="#p0.id")
    @UpdateProvider(type=CrudProvider.class, method =MyProvider.UPDATE)
    int update(Account account);

    @CacheEvict(value = "accounts",key="#p0.id")
    @UpdateProvider(type=CrudProvider.class, method = MyProvider.UPDATE)
    int updateForm(AccountForm accountForm);

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @SelectProvider(type=CrudProvider.class, method =MyProvider.FIND_ALL)
    List<Account> findAll();

    Account findByLoginName(String loginName);

    List<Account> findByLoginNameLikeAndType(@Param("p")Map<String,Object> map);

    String findAccountPassword(String id);

    List<Account> findByOfficeIds(List<String> officeIds);

    List<Account> findByPosition(String positionId);

    Page<AccountDto> findPage(Pageable pageable, @Param("p")AccountQuery accountQuery);

    List<Account> findByFilter(@Param("p")AccountQuery accountQuery);

    int saveAccountOffice(@Param("accountId")String accountId,@Param("officeIds")List<String> officeIds);

    int deleteAccountOffice(@Param("accountId")String accountId);

    List<Account> findLabels(List<String> ids);

    List<Account> findByLoginNameList(List<String> loginNames);

    List<String> findAccountOfficeByIds(List<String> accountId);
}

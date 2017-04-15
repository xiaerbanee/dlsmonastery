package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@Mapper
public interface AccountMapper extends MyMapper<Account,String> {

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

    List<NameValueDto> findAccountOfficeByIds(List<String> accountId);
}

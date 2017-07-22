package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.repository.AccountRepository;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.repository.RoleRepository;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangzm on 2017/5/10.
 */
@Component
public class RoleManager {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<String> findByAccountId(String accountId){
        Account account=accountRepository.findOne(accountId);
        if(StringUtils.isNotBlank(account.getRoleIds())){
           return StringUtils.getSplitList(account.getOfficeIds(), CharConstant.COMMA);
        }
        return null;
    }
}

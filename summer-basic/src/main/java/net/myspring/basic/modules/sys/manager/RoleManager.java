package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.hr.repository.AccountRepository;
import net.myspring.basic.modules.sys.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wangzm on 2017/5/10.
 */
@Component
public class RoleManager {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

}

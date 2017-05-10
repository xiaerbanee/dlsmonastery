package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wangzm on 2017/5/10.
 */
@Component
public class RoleManager {

    @Autowired
    private RoleMapper roleMapper;

    public String findIdByAccountId(String accountId){
        Role role=roleMapper.findByAccountId(accountId);
        if(role!=null){
            return role.getId();
        }
        return null;
    }
}

package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.mapper.RoleMapper;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.form.RoleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Component
@CacheConfig(cacheNames = "roles")
public class RoleManager {

    @Autowired
    private RoleMapper roleMapper;

    @Cacheable(key="#p0")
    public Role findOne(String id){
        return roleMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Role save(Role role){
        roleMapper.save(role);
        return  role;
    }

    @CachePut(key="#p0.id")
    public Role update(Role role){
        roleMapper.update(role);
        return  roleMapper.findOne(role.getId());
    }

    @CachePut(key="#p0.id")
    public Role updateForm(RoleForm roleForm){
        roleMapper.updateForm(roleForm);
        return  roleMapper.findOne(roleForm.getId());
    }
}

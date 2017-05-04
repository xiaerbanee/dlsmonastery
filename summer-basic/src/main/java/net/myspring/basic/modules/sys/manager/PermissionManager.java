package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "permissions")
public class PermissionManager {

    @Autowired
    private PermissionMapper permissionMapper;

    @Cacheable(key="#p0")
    public Permission findOne(String id) {
        return permissionMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Permission save(Permission permission){
        permissionMapper.save(permission);
        return  permission;
    }

    @CachePut(key="#p0.id")
    public Permission update(Permission permission){
        permissionMapper.update(permission);
        return  permissionMapper.findOne(permission.getId());
    }

    @CachePut(key="#p0.id")
    public Permission updateForm(PermissionForm permissionForm){
        permissionMapper.updateForm(permissionForm);
        return  permissionMapper.findOne(permissionForm.getId());
    }
}

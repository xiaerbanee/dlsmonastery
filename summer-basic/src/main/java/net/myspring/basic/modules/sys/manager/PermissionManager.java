package net.myspring.basic.modules.sys.manager;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class PermissionManager {

    @Autowired
    private PermissionMapper permissionMapper;

    @Cacheable(value = "permissions",key="#p0")
    public Permission findOne(String id) {
        return permissionMapper.findOne(id);
    }

    @CachePut(value = "permissions",key="#p0.id")
    public Permission save(Permission permission){
        permissionMapper.save(permission);
        return  permission;
    }

    @CachePut(value = "permissions",key="#p0.id")
    public Permission update(Permission permission){
        permissionMapper.update(permission);
        return  permissionMapper.findOne(permission.getId());
    }

    @CacheEvict(value = "permissions",key="#p0")
    public int deleteById(String id) {
        return permissionMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }

}

package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.modules.hr.web.form.PositionModuleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by wangzm on 2017/4/19.
 */
@Component
@CacheConfig(cacheNames = "positionModules")
public class RoleModuleManager {
    
    @Autowired
    private RoleModuleManager positionModuleManager;

    @Cacheable(key="#p0")
    public RoleModule findOne(String id) {
        return positionModuleManager.findOne(id);
    }

    @CachePut(key="#p0.id")
    public RoleModule save(RoleModule positionBackend){
        positionModuleManager.save(positionBackend);
        return  positionBackend;
    }

    @CachePut(key="#p0.id")
    public RoleModule update(RoleModule positionBackend){
        positionModuleManager.update(positionBackend);
        return  positionModuleManager.findOne(positionBackend.getId());
    }

    @CachePut(key="#p0.id")
    public RoleModule updateForm(PositionModuleForm positionBackendForm){
        positionModuleManager.updateForm(positionBackendForm);
        return  positionModuleManager.findOne(positionBackendForm.getId());
    }
}

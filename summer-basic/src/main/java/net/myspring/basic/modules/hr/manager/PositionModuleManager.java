package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.PositionModule;
import net.myspring.basic.modules.hr.web.form.PositionModuleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by wangzm on 2017/4/19.
 */
@Component
public class PositionModuleManager {
    
    @Autowired
    private PositionModuleManager positionModuleManager;

    @Cacheable(value = "positionModules",key="#p0")
    public PositionModule findOne(String id) {
        return positionModuleManager.findOne(id);
    }

    @CachePut(value = "positionModules",key="#p0.id")
    public PositionModule save(PositionModule positionBackend){
        positionModuleManager.save(positionBackend);
        return  positionBackend;
    }

    @CachePut(value = "positionModules",key="#p0.id")
    public PositionModule update(PositionModule positionBackend){
        positionModuleManager.update(positionBackend);
        return  positionModuleManager.findOne(positionBackend.getId());
    }

    @CachePut(value = "positionModules",key="#p0.id")
    public PositionModule updateForm(PositionModuleForm positionBackendForm){
        positionModuleManager.updateForm(positionBackendForm);
        return  positionModuleManager.findOne(positionBackendForm.getId());
    }
}

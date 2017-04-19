package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.PositionBackend;
import net.myspring.basic.modules.hr.web.form.PositionBackendForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by wangzm on 2017/4/19.
 */
@Component
public class PositionBackendManager {
    
    @Autowired
    private PositionBackendManager positionBackendManager;

    @Cacheable(value = "positionBackends",key="#p0")
    public PositionBackend findOne(String id) {
        return positionBackendManager.findOne(id);
    }

    @CachePut(value = "positionBackends",key="#p0.id")
    public PositionBackend save(PositionBackend positionBackend){
        positionBackendManager.save(positionBackend);
        return  positionBackend;
    }

    @CachePut(value = "positionBackends",key="#p0.id")
    public PositionBackend update(PositionBackend positionBackend){
        positionBackendManager.update(positionBackend);
        return  positionBackendManager.findOne(positionBackend.getId());
    }

    @CachePut(value = "positionBackends",key="#p0.id")
    public PositionBackend updateForm(PositionBackendForm positionBackendForm){
        positionBackendManager.updateForm(positionBackendForm);
        return  positionBackendManager.findOne(positionBackendForm.getId());
    }
}

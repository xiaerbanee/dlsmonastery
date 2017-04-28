package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.mapper.DemoPhoneTypeMapper;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
@CacheConfig(cacheNames = "demoPhoneTypes")
public class DemoPhoneTypeManager {
    @Autowired
    private DemoPhoneTypeMapper demoPhoneTypeMapper;

    @Cacheable(key="#p0")
    public DemoPhoneType findOne(String id) {
        return demoPhoneTypeMapper.findOne(id);
    }

    @Cacheable(key="#p0.id")
    public DemoPhoneType save(DemoPhoneType demoPhoneType){
        demoPhoneTypeMapper.save(demoPhoneType);
        return  demoPhoneType;
    }

    @CachePut(key="#p0.id")
    public DemoPhoneType update(DemoPhoneType demoPhoneType){
        demoPhoneTypeMapper.update(demoPhoneType);
        return  demoPhoneTypeMapper.findOne(demoPhoneType.getId());
    }

    @CachePut(key="#p0.id")
    public DemoPhoneType updateForm(DemoPhoneTypeForm demoPhoneTypeForm){
        demoPhoneTypeMapper.updateForm(demoPhoneTypeForm);
        return  demoPhoneTypeMapper.findOne(demoPhoneTypeForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return demoPhoneTypeMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

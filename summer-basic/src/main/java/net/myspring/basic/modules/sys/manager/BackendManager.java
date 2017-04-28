package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.mapper.BackendMapper;
import net.myspring.basic.modules.sys.web.form.BackendForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangzm on 2017/4/19.
 */
@Component
@CacheConfig(cacheNames = "backends")
public class BackendManager {
    
    @Autowired
    private BackendMapper backendMapper;

    @Cacheable(key="#p0")
    public Backend findOne(String id){
        return backendMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Backend save(Backend backend){
        backendMapper.save(backend);
        return  backend;
    }

    @CachePut(key="#p0.id")
    public Backend update(Backend backend){
        backendMapper.update(backend);
        return  backendMapper.findOne(backend.getId());
    }

    @CachePut(key="#p0.id")
    public Backend updateForm(BackendForm backendForm){
        backendMapper.updateForm(backendForm);
        return  backendMapper.findOne(backendForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return backendMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

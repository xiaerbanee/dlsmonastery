package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.mapper.BackendModuleMapper;
import net.myspring.basic.modules.sys.web.form.BackendModuleForm;
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
@CacheConfig(cacheNames = "backendModules")
public class BackendModuleManager {

    @Autowired
    private BackendModuleMapper backendModuleMapper;

    @Cacheable(key="#p0")
    public BackendModule findOne(String id){
        return backendModuleMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public BackendModule save(BackendModule backend){
        backendModuleMapper.save(backend);
        return  backend;
    }

    @CachePut(key="#p0.id")
    public BackendModule update(BackendModule backend){
        backendModuleMapper.update(backend);
        return  backendModuleMapper.findOne(backend.getId());
    }

    @CachePut(key="#p0.id")
    public BackendModule updateForm(BackendModuleForm backendModuleForm){
        backendModuleMapper.updateForm(backendModuleForm);
        return  backendModuleMapper.findOne(backendModuleForm.getId());
    }
}

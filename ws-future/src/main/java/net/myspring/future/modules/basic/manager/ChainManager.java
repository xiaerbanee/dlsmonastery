package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Chain;
import net.myspring.future.modules.basic.mapper.ChainMapper;
import net.myspring.future.modules.basic.web.form.ChainForm;
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
@CacheConfig(cacheNames = "chains")
public class ChainManager {
    @Autowired
    private ChainMapper chainMapper;

    @Cacheable(key="#p0")
    public Chain findOne(String id) {
        return chainMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Chain save(Chain chain){
        chainMapper.save(chain);
        return chain;
    }

    @CachePut(key="#p0.id")
    public Chain update(Chain chain){
        chainMapper.update(chain);
        return  chainMapper.findOne(chain.getId());
    }

    @CachePut(key="#p0.id")
    public Chain updateForm(ChainForm chainForm){
        chainMapper.updateForm(chainForm);
        return  chainMapper.findOne(chainForm.getId());
    }
}

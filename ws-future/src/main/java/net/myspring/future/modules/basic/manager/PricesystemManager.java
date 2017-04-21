package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.mapper.PricesystemMapper;
import net.myspring.future.modules.basic.web.form.PricesystemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class PricesystemManager {
    @Autowired
    private PricesystemMapper pricesystemMapper;

    @Cacheable(value = "pricesystems",key="#p0")
    public Pricesystem findOne(String id) {
        return pricesystemMapper.findOne(id);
    }

    @Cacheable(value = "pricesystems",key="#p0.id")
    public Pricesystem save(Pricesystem pricesystem){
        pricesystemMapper.save(pricesystem);
        return  pricesystem;
    }

    @CachePut(value = "pricesystems",key="#p0.id")
    public Pricesystem update(Pricesystem pricesystem){
        pricesystemMapper.update(pricesystem);
        return  pricesystemMapper.findOne(pricesystem.getId());
    }

    @CachePut(value = "pricesystems",key="#p0.id")
    public Pricesystem updateForm(PricesystemForm pricesystemForm){
        pricesystemMapper.updateForm(pricesystemForm);
        return  pricesystemMapper.findOne(pricesystemForm.getId());
    }

    @CacheEvict(value = "pricesystems",key="#p0")
    public int deleteById(String id) {
        return pricesystemMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.web.form.AdPricesystemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
public class AdPricesystemManager {
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;

    @Cacheable(value = "adPricesystems",key="#p0")
    public AdPricesystem findOne(String id) {
        return adPricesystemMapper.findOne(id);
    }

    @Cacheable(value = "adPricesystems",key="#p0.id")
    public AdPricesystem save(AdPricesystem adPricesystem){
        adPricesystemMapper.save(adPricesystem);
        return  adPricesystem;
    }

    @CachePut(value = "adPricesystems",key="#p0.id")
    public AdPricesystem update(AdPricesystem adPricesystem){
        adPricesystemMapper.update(adPricesystem);
        return  adPricesystemMapper.findOne(adPricesystem.getId());
    }

    @CachePut(value = "adPricesystems",key="#p0.id")
    public AdPricesystem updateForm(AdPricesystemForm adPricesystemForm){
        adPricesystemMapper.updateForm(adPricesystemForm);
        return  adPricesystemMapper.findOne(adPricesystemForm.getId());
    }

    @CacheEvict(value = "adPricesystems",key="#p0")
    public int deleteById(String id) {
        return adPricesystemMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

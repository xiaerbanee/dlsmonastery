package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.web.form.PricesystemDetailForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
@Component
@CacheConfig(cacheNames = "pricesystemDetails")
public class PricesystemDetailManager {
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;

    @Cacheable(key="#p0")
    public PricesystemDetail findOne(String id) {
        return pricesystemDetailMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public PricesystemDetail save(PricesystemDetail pricesystemDetail){
        pricesystemDetailMapper.save(pricesystemDetail);
        return  pricesystemDetail;
    }

    @CachePut(key="#p0.id")
    public PricesystemDetail update(PricesystemDetail pricesystemDetail){
        pricesystemDetailMapper.update(pricesystemDetail);
        return  pricesystemDetailMapper.findOne(pricesystemDetail.getId());
    }

    @CachePut(key="#p0.id")
    public PricesystemDetail updateForm(PricesystemDetailForm pricesystemDetailForm){
        pricesystemDetailMapper.updateForm(pricesystemDetailForm);
        return  pricesystemDetailMapper.findOne(pricesystemDetailForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return pricesystemDetailMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

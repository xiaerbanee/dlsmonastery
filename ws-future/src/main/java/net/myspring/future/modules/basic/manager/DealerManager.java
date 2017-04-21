package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.mapper.DealerMapper;
import net.myspring.future.modules.basic.web.form.DealerForm;
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
public class DealerManager {
    @Autowired
    private DealerMapper dealerMapper;

    @Cacheable(value = "accounts",key="#p0")
    public Dealer findOne(String id) {
        return dealerMapper.findOne(id);
    }

    @Cacheable(value = "dealers",key="#p0.id")
    public Dealer save(Dealer dealer){
        dealerMapper.save(dealer);
        return  dealer;
    }

    @CachePut(value = "dealers",key="#p0.id")
    public Dealer update(Dealer dealer){
        dealerMapper.update(dealer);
        return  dealerMapper.findOne(dealer.getId());
    }

    @CachePut(value = "dealers",key="#p0.id")
    public Dealer updateForm(DealerForm dealerForm){
        dealerMapper.updateForm(dealerForm);
        return  dealerMapper.findOne(dealerForm.getId());
    }

    @CacheEvict(value = "dealers",key="#p0")
    public int deleteById(String id) {
        return dealerMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

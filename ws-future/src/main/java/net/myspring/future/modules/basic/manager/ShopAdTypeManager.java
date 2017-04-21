package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.mapper.ShopAdTypeMapper;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
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
public class ShopAdTypeManager {
    @Autowired
    private ShopAdTypeMapper shopAdTypeMapper;

    @Cacheable(value = "shopAdTypes",key="#p0")
    public ShopAdType findOne(String id) {
        return shopAdTypeMapper.findOne(id);
    }

    @Cacheable(value = "shopAdTypes",key="#p0.id")
    public ShopAdType save(ShopAdType shopAdType){
        shopAdTypeMapper.save(shopAdType);
        return  shopAdType;
    }

    @CachePut(value = "shopAdTypes",key="#p0.id")
    public ShopAdType update(ShopAdType shopAdType){
        shopAdTypeMapper.update(shopAdType);
        return  shopAdTypeMapper.findOne(shopAdType.getId());
    }

    @CachePut(value = "shopAdTypes",key="#p0.id")
    public ShopAdType updateForm(ShopAdTypeForm shopAdTypeForm){
        shopAdTypeMapper.updateForm(shopAdTypeForm);
        return  shopAdTypeMapper.findOne(shopAdTypeForm.getId());
    }

    @CacheEvict(value = "shopAdTypes",key="#p0")
    public int deleteById(String id) {
        return shopAdTypeMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

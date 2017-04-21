package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
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
public class ProductTypeManager {
    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Cacheable(value = "productTypes",key="#p0")
    public ProductType findOne(String id) {
        return productTypeMapper.findOne(id);
    }

    @Cacheable(value = "productTypes",key="#p0.id")
    public ProductType save(ProductType productType){
        productTypeMapper.save(productType);
        return  productType;
    }

    @CachePut(value = "productTypes",key="#p0.id")
    public ProductType update(ProductType productType){
        productTypeMapper.update(productType);
        return  productTypeMapper.findOne(productType.getId());
    }

    @CachePut(value = "productTypes",key="#p0.id")
    public ProductType updateForm(ProductTypeForm productTypeForm){
        productTypeMapper.updateForm(productTypeForm);
        return  productTypeMapper.findOne(productTypeForm.getId());
    }

    @CacheEvict(value = "productTypes",key="#p0")
    public int deleteById(String id) {
        return productTypeMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.web.form.ProductForm;
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
@CacheConfig(cacheNames = "products")
public class ProductManager {
    @Autowired
    private ProductMapper productMapper;

    @Cacheable(key="#p0")
    public Product findOne(String id) {
        return productMapper.findOne(id);
    }

    @Cacheable(key="#p0.id")
    public Product save(Product product){
        productMapper.save(product);
        return  product;
    }

    @CachePut(key="#p0.id")
    public Product update(Product product){
        productMapper.update(product);
        return  productMapper.findOne(product.getId());
    }

    @CachePut(key="#p0.id")
    public Product updateForm(ProductForm productForm){
        productMapper.updateForm(productForm);
        return  productMapper.findOne(productForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return productMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}

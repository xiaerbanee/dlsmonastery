package net.myspring.future.modules.basic.manager;

import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

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
}

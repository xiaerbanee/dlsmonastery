package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.domain.Product;
import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.mapper.ProductMapper;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/4/5.
 */
@Service
@LocalDataSource
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public ProductDto findOne(String id) {
        Product product = productMapper.findOne(id);
        ProductDto productDto =  BeanUtil.map(product, ProductDto.class);
        return productDto;
    }
}

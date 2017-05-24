package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.mapper.ProductImeSaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ProductImeSaleService {

    @Autowired
    private ProductImeSaleMapper productImeSaleMapper;


    public ProductImeSale findOne(String id) {
        return productImeSaleMapper.findOne(id);
    }

    public Page<ProductImeSale> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ProductImeSale> page = productImeSaleMapper.findPage(pageable, map);
        return page;
    }

    @Transactional
    public void save(ProductImeSale productImeSale) {
    }

    public void saleBack(ProductImeSale productImeSale){
    }
}

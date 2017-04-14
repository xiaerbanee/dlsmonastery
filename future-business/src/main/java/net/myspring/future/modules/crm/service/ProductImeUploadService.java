package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.mapper.ProductImeUploadMapper;
import net.myspring.future.modules.crm.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadMapper productImeUploadMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImeMapper productImeMapper;

    public ProductImeUpload findOne(String id){
        return productImeUploadMapper.findOne(id);
    }

    public Page<ProductImeUpload> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ProductImeUpload> page = productImeUploadMapper.findPage(pageable, map);
        return page;
    }

    @Transactional
    public void save(ProductImeUpload productImeUpload){
    }

    @Transactional
    public void audit(String[] ids,Boolean pass){
    }

}

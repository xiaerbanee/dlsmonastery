package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.mapper.ProductImeUploadMapper;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadMapper productImeUploadMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImeMapper productImeMapper;

    @Autowired
    private CacheUtils cacheUtils;

    public ProductImeUpload findOne(String id){
        return productImeUploadMapper.findOne(id);
    }

    public Page<ProductImeUploadDto> findPage(Pageable pageable, ProductImeUploadQuery productImeUploadQuery) {

        productImeUploadQuery.setPageable(pageable);
        List<ProductImeUploadDto> list = productImeUploadMapper.findList(productImeUploadQuery);
        cacheUtils.initCacheInput(list);

        Page<ProductImeUploadDto> page = new PageImpl(list,pageable,(pageable.getPageNumber()+100)*pageable.getPageSize());
        return page;
    }

    @Transactional
    public void save(ProductImeUpload productImeUpload){
    }

    @Transactional
    public void audit(String[] ids,Boolean pass){
    }

}

package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.mapper.ProductImeUploadMapper;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadMapper productImeUploadMapper;

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


    public void save(ProductImeUpload productImeUpload){
    }


    public void audit(String[] ids,Boolean pass){
    }

    public void batchAudit(String[] ids, String pass) {

        if(ids == null || ids.length == 0){
            return;
        }

        List<ProductImeUpload> productImeUploads = productImeUploadMapper.findByIds(Lists.newArrayList(ids));
        if(CollectionUtil.isEmpty(productImeUploads)){
            return;
        }

        for (ProductImeUpload each : productImeUploads) {
            each.setStatus("1".equals(pass) ? AuditStatusEnum.已通过.name() : AuditStatusEnum.未通过.name());
            productImeUploadMapper.update(each);
        }
    }
}

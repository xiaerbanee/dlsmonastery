package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.repository.ProductImeUploadRepository;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadRepository productImeUploadRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public ProductImeUpload findOne(String id){
        return productImeUploadRepository.findOne(id);
    }

    public Page<ProductImeUploadDto> findPage(Pageable pageable, ProductImeUploadQuery productImeUploadQuery) {

        Page<ProductImeUploadDto> page = productImeUploadRepository.findPage(pageable, productImeUploadQuery);
        cacheUtils.initCacheInput(page.getContent());

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

        List<ProductImeUpload> productImeUploads = productImeUploadRepository.findAll(Lists.newArrayList(ids));
        if(CollectionUtil.isEmpty(productImeUploads)){
            return;
        }

        for (ProductImeUpload each : productImeUploads) {
            each.setStatus("1".equals(pass) ? AuditStatusEnum.已通过.name() : AuditStatusEnum.未通过.name());
            productImeUploadRepository.save(each);
        }
    }
}

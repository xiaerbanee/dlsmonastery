package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.domain.PriceChangeProduct;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.mapper.PriceChangeImeMapper;
import net.myspring.future.modules.crm.mapper.PriceChangeMapper;
import net.myspring.future.modules.crm.mapper.PriceChangeProductMapper;
import net.myspring.future.modules.crm.web.form.PriceChangeForm;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PriceChangeService {

    @Autowired
    private PriceChangeMapper priceChangeMapper;
    @Autowired
    private PriceChangeProductMapper priceChangeProductMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private PriceChangeImeMapper priceChangeImeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public PriceChange findNearPriceChange(){
        return priceChangeMapper.findNearPriceChange();
    }


    public List<PriceChange> findAllByEnabledAndDate(LocalDateTime uploadEndDate) {
        return priceChangeMapper.finAllByEnabled(uploadEndDate);
    }

    public PriceChangeForm findForm(PriceChangeForm priceChangeForm){
        if(!priceChangeForm.isCreate()){
            PriceChange priceChange = priceChangeMapper.findOne(priceChangeForm.getId());
            priceChangeForm = BeanUtil.map(priceChange,PriceChangeForm.class);
            List<PriceChangeProduct> priceChangeProducts = priceChangeProductMapper.findByPriceChangeId(priceChangeForm.getId());
            List<ProductTypeDto> productTypeDtos = BeanUtil.map(productTypeMapper.findLabels(CollectionUtil.extractToList(priceChangeProducts,"id")),ProductTypeDto.class);
            priceChangeForm.setProductTypeDtos(productTypeDtos);
            priceChangeForm.setProductTypeIdList(CollectionUtil.extractToList(productTypeDtos,"id"));
        }else{
            List<ProductTypeDto> allProductTypeDtos = BeanUtil.map(productTypeMapper.findAllEnabled(),ProductTypeDto.class);
            priceChangeForm.setAllProductTypeDtos(allProductTypeDtos);
        }
        return priceChangeForm;
    }


    public Page<PriceChangeDto> findPage(Pageable pageable, PriceChangeQuery priceChangeQuery) {
        Page<PriceChangeDto> page = priceChangeMapper.findPage(pageable, priceChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void save(PriceChange priceChange){
    }

    @Transactional
    public void delete(PriceChange priceChange){
        priceChange.setEnabled(false);
        priceChange.setName(priceChange.getName()+ LocalDate.now()+"废除");
        priceChangeMapper.update(priceChange);
    }

    @Transactional
    public void check(PriceChange priceChange){
    }


}

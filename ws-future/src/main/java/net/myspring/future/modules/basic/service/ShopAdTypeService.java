package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.repository.ShopAdTypeRepository;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.future.modules.basic.web.query.ShopAdTypeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopAdTypeService {

    @Autowired
    private ShopAdTypeRepository shopAdTypeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ShopAdTypeDto> findAllByEnabled() {
        List<ShopAdTypeDto> shopAdTypes = shopAdTypeRepository.findAllByEnabled();
        return shopAdTypes;
    }

    public ShopAdType findOne(String id) {
        ShopAdType shopAdType = shopAdTypeRepository.findOne(id);
        return shopAdType;
    }

    public ShopAdTypeForm getForm(ShopAdTypeForm shopAdTypeForm){
        if(!shopAdTypeForm.isCreate()){
            ShopAdType shopAdType=shopAdTypeRepository.findOne(shopAdTypeForm.getId());
            shopAdTypeForm=BeanUtil.map(shopAdType,ShopAdTypeForm.class);
            cacheUtils.initCacheInput(shopAdTypeForm);
        }
        return shopAdTypeForm;
    }

    public Page<ShopAdTypeDto> findPage(Pageable pageable, ShopAdTypeQuery shopAdTypeQuery) {
        Page<ShopAdTypeDto> page = shopAdTypeRepository.findPage(pageable, shopAdTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopAdType save(ShopAdTypeForm shopAdTypeForm) {
        ShopAdType shopAdType;
        if (shopAdTypeForm.isCreate()) {
            shopAdType= BeanUtil.map(shopAdTypeForm,ShopAdType.class);
            shopAdTypeRepository.save(shopAdType);
        } else {
            shopAdType= shopAdTypeRepository.findOne(shopAdTypeForm.getId());
            ReflectionUtil.copyProperties(shopAdTypeForm,shopAdType);
            shopAdTypeRepository.save(shopAdType);
        }
        return shopAdType;
    }

    public void delete(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeRepository.logicDelete(shopAdTypeForm.getId());
    }

    public List<String> findTotalPriceTypeList() {
        return TotalPriceTypeEnum.getValues();
    }
}

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
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopAdTypeService {

    @Autowired
    private ShopAdTypeRepository shopAdTypeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public ShopAdTypeDto findOne(String id) {
        ShopAdTypeDto shopAdTypeDto = new ShopAdTypeDto();
        if(StringUtils.isNotBlank(id)){
            ShopAdType shopAdType = shopAdTypeRepository.findOne(id);
            shopAdTypeDto = BeanUtil.map(shopAdType,ShopAdTypeDto.class);
            cacheUtils.initCacheInput(shopAdTypeDto);
        }
        return shopAdTypeDto;
    }

    public ShopAdTypeForm getForm(ShopAdTypeForm shopAdTypeForm){
        shopAdTypeForm.getExtra().put("totalPriceTypeList",TotalPriceTypeEnum.getValues());
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

    public ShopAdTypeQuery getQuery(ShopAdTypeQuery shopAdTypeQuery) {
        shopAdTypeQuery.getExtra().put("totalPriceTypeList",TotalPriceTypeEnum.getValues());
        return shopAdTypeQuery;
    }
}

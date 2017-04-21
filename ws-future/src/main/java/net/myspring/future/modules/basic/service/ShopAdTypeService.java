package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.dto.ShopAdTypeDto;
import net.myspring.future.modules.basic.manager.ShopAdTypeManager;
import net.myspring.future.modules.basic.mapper.ShopAdTypeMapper;
import net.myspring.future.modules.basic.web.Query.ShopAdTypeQuery;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShopAdTypeService {

    @Autowired
    private ShopAdTypeMapper shopAdTypeMapper;
    @Autowired
    private ShopAdTypeManager shopAdTypeManager;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ShopAdType> findAllByEnabled() {
        List<ShopAdType> shopAdTypes = shopAdTypeMapper.findAllByEnabled();
        return shopAdTypes;
    }

    public ShopAdType findOne(String id) {
        ShopAdType shopAdType = shopAdTypeMapper.findOne(id);
        return shopAdType;
    }

    public ShopAdTypeForm findForm(ShopAdTypeForm shopAdTypeForm){
        if(!shopAdTypeForm.isCreate()){
            ShopAdType shopAdType=shopAdTypeManager.findOne(shopAdTypeForm.getId());
            shopAdTypeForm=BeanUtil.map(shopAdType,ShopAdTypeForm.class);
            cacheUtils.initCacheInput(shopAdTypeForm);
        }
        return shopAdTypeForm;
    }

    public Page<ShopAdTypeDto> findPage(Pageable pageable, ShopAdTypeQuery shopAdTypeQuery) {
        Page<ShopAdTypeDto> page = shopAdTypeMapper.findPage(pageable, shopAdTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopAdType save(ShopAdTypeForm shopAdTypeForm) {
        ShopAdType shopAdType;
        if (shopAdTypeForm.isCreate()) {
            shopAdType= BeanUtil.map(shopAdTypeForm,ShopAdType.class);
            shopAdType= shopAdTypeManager.save(shopAdType);
        } else {
            shopAdType= shopAdTypeManager.updateForm(shopAdTypeForm);
        }
        return shopAdType;
    }

    public void delete(ShopAdTypeForm shopAdTypeForm) {
        shopAdTypeMapper.logicDeleteOne(shopAdTypeForm.getId());
    }
}

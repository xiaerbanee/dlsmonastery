package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopPromotion;
import net.myspring.future.modules.layout.dto.ShopPromotionDto;
import net.myspring.future.modules.layout.mapper.ShopPromotionMapper;
import net.myspring.future.modules.layout.web.form.ShopPromotionForm;
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@Transactional
public class ShopPromotionService {

    @Autowired
    private ShopPromotionMapper shopPromotionMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ShopPromotionDto> findPage(Pageable pageable, ShopPromotionQuery shopPromotionQuery){
        Page<ShopPromotionDto> page = shopPromotionMapper.findPage(pageable,shopPromotionQuery);
        cacheUtils.initCacheInput(page.getContent());
        return  page;
    }

    public ShopPromotion save(ShopPromotionForm shopPromotionForm){
        ShopPromotion shopPromotion;
        if(shopPromotionForm.isCreate()){
            shopPromotion = BeanUtil.map(shopPromotionForm,ShopPromotion.class);
            String maxBusinessId = shopPromotionMapper.findMaxBusinessId(LocalDate.now());
            shopPromotion.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
            shopPromotionMapper.save(shopPromotion);
        }else{
            shopPromotion = shopPromotionMapper.findOne(shopPromotionForm.getId());
            ReflectionUtil.copyProperties(shopPromotionForm,shopPromotion);
            shopPromotionMapper.update(shopPromotion);
        }
        return shopPromotion;
    }

    public ShopPromotionForm getForm(ShopPromotionForm shopPromotionForm){
        if(!shopPromotionForm.isCreate()){
            ShopPromotion shopPromotion = shopPromotionMapper.findOne(shopPromotionForm.getId());
            shopPromotionForm = BeanUtil.map(shopPromotion,ShopPromotionForm.class);
            cacheUtils.initCacheInput(shopPromotionForm);
        }
        return shopPromotionForm;
    }

    public void logicDeleteOne(String id){
        shopPromotionMapper.logicDeleteOne(id);
    }
}

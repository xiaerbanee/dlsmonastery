package net.myspring.future.modules.layout.service;

import net.myspring.future.common.enums.ActivityTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.manager.RedisIdManager;
import net.myspring.future.modules.layout.domain.ShopPromotion;
import net.myspring.future.modules.layout.dto.ShopPromotionDto;
import net.myspring.future.modules.layout.repository.ShopPromotionRepository;
import net.myspring.future.modules.layout.web.form.ShopPromotionForm;
import net.myspring.future.modules.layout.web.query.ShopPromotionQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class ShopPromotionService {

    @Autowired
    private ShopPromotionRepository shopPromotionRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisIdManager redisIdManager;

    public Page<ShopPromotionDto> findPage(Pageable pageable, ShopPromotionQuery shopPromotionQuery){
        Page<ShopPromotionDto> page = shopPromotionRepository.findPage(pageable,shopPromotionQuery);
        cacheUtils.initCacheInput(page.getContent());
        return  page;
    }

    @Transactional
    public ShopPromotion save(ShopPromotionForm shopPromotionForm){
        ShopPromotion shopPromotion;
        if(shopPromotionForm.isCreate()){
            shopPromotion = BeanUtil.map(shopPromotionForm,ShopPromotion.class);
            LocalDate now = LocalDate.now();
            shopPromotion.setBusinessId(redisIdManager.getNextShopPromotionBusinessId(now));
            shopPromotionRepository.save(shopPromotion);
        }else{
            shopPromotion = shopPromotionRepository.findOne(shopPromotionForm.getId());
            ReflectionUtil.copyProperties(shopPromotionForm,shopPromotion);
            shopPromotionRepository.save(shopPromotion);
        }
        return shopPromotion;
    }

    public ShopPromotionForm getForm(ShopPromotionForm shopPromotionForm){
        shopPromotionForm.getExtra().put("activityTypeList",ActivityTypeEnum.getList());
        return shopPromotionForm;
    }

    public ShopPromotionDto findOne(String id){
        ShopPromotionDto shopPromotionDto = new ShopPromotionDto();
        if(StringUtils.isNotBlank(id)){
            ShopPromotion shopPromotion = shopPromotionRepository.findOne(id);
            shopPromotionDto  = BeanUtil.map(shopPromotion,ShopPromotionDto.class);
            cacheUtils.initCacheInput(shopPromotionDto);
        }
        return shopPromotionDto;
    }

    @Transactional
    public void logicDelete(String id){
        shopPromotionRepository.logicDelete(id);
    }
}

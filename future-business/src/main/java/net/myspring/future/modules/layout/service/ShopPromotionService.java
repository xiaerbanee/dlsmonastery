package net.myspring.future.modules.layout.service;

import net.myspring.future.modules.layout.domain.ShopPromotion;
import net.myspring.future.modules.layout.mapper.ShopPromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ShopPromotionService {

    @Autowired
    private ShopPromotionMapper shopPromotionMapper;

    public Page<ShopPromotion> findPage(Pageable pageable, Map<String,Object> map){
        Page<ShopPromotion> page = shopPromotionMapper.findPage(pageable,map);
        return  page;
    }

    public ShopPromotion save(ShopPromotion shopPromotion){
        return shopPromotion;
    }

    public ShopPromotion findOne(String id){
        ShopPromotion shopPromotion= shopPromotionMapper.findOne(id);
        return shopPromotion;
    }

    public void logicDeleteOne(String id){
        shopPromotionMapper.logicDeleteOne(id);
    }
}

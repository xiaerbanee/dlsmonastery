package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.repository.ShopAllotDetailRepository;
import net.myspring.future.modules.layout.repository.ShopAllotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopAllotDetailService {

    @Autowired
    private ShopAllotDetailRepository shopAllotDetailRepository;
    @Autowired
    private ShopAllotRepository shopAllotRepository;

    @Autowired
    private CacheUtils cacheUtils;



    public List<ShopAllotDetailDto> getShopAllotDetailListForNew(String fromDepotId, String toDepotId) {

        List<ShopAllotDetailDto> result = shopAllotDetailRepository.getShopAllotDetailListForNew(fromDepotId, toDepotId);

        cacheUtils.initCacheInput(result);

        return result;

    }

    public List<ShopAllotDetailDto> getShopAllotDetailListForEdit(String shopAllotId) {

        ShopAllot shopAllot = shopAllotRepository.findOne(shopAllotId);

        List<ShopAllotDetailDto> result = shopAllotDetailRepository.getShopAllotDetailListForEdit(shopAllotId, shopAllot.getFromShopId(), shopAllot.getToShopId());

        cacheUtils.initCacheInput(result);

        return result;

    }

}

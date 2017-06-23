package net.myspring.future.modules.layout.service;

import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.dto.ShopAllotDetailSimpleDto;
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

    public List<ShopAllotDetailSimpleDto> getShopAllotDetailListForNew(String fromDepotId, String toDepotId) {
        return shopAllotDetailRepository.getShopAllotDetailListForNew(fromDepotId, toDepotId);
    }

    public List<ShopAllotDetailSimpleDto> getShopAllotDetailListForEdit(String shopAllotId) {
        ShopAllot shopAllot = shopAllotRepository.findOne(shopAllotId);
        return shopAllotDetailRepository.getShopAllotDetailListForEdit(shopAllotId, shopAllot.getFromShopId(), shopAllot.getToShopId());
    }

}

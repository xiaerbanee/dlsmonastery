package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.manager.ShopAllotDetailManager;
import net.myspring.future.modules.layout.repository.ShopAllotDetailRepository;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
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
    private ShopAllotDetailManager shopAllotDetailManager;
    @Autowired
    private CacheUtils cacheUtils;



    public List<ShopAllotDetailForm> getShopAllotDetailListForNewOrEdit(String shopAllotId, String fromDepotId, String toDepotId) {
        return shopAllotDetailManager.getShopAllotDetailListForNewOrEdit(shopAllotId, fromDepotId, toDepotId);
    }

}

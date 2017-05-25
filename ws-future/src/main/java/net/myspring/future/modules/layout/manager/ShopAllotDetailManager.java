package net.myspring.future.modules.layout.manager;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.mapper.ShopAllotDetailMapper;
import net.myspring.future.modules.layout.repository.ShopAllotDetailRepository;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liuj on 2017/5/15.
 */
@Component
public class ShopAllotDetailManager {

    @Autowired
    private ShopAllotDetailRepository shopAllotDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ShopAllotDetailForm> getShopAllotDetailListForNewOrEdit(String shopAllotId, String fromDepotId, String toDepotId) {

        List<ShopAllotDetailDto> result = shopAllotDetailRepository.getShopAllotDetailListForNewOrEdit(shopAllotId, fromDepotId, toDepotId);
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, ShopAllotDetailForm.class);
    }

    public void batchSave(List<ShopAllotDetail> shopAllotDetails) {
        if(shopAllotDetails==null || shopAllotDetails.isEmpty()){
            return;
        }
        shopAllotDetailRepository.save(shopAllotDetails);
    }

    public List<ShopAllotDetailForm> getShopAllotDetailListForViewOrAudit(String shopAllotId) {
        List<ShopAllotDetailDto> result = shopAllotDetailRepository.getShopAllotDetailListForViewOrAudit(shopAllotId);
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, ShopAllotDetailForm.class);
    }


}

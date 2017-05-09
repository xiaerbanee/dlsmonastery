package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.mapper.ShopAllotDetailMapper;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopAllotDetailService {

    @Autowired
    private ShopAllotDetailMapper shopAllotDetailMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public List<ShopAllotDetailForm> getShopAllotDetailListForEdit(String shopAllotId, String fromShopId, String toShopId) {
        List<ShopAllotDetailDto> result = shopAllotDetailMapper.getShopAllotDetailListForEdit(shopAllotId, fromShopId, toShopId);
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, ShopAllotDetailForm.class);
    }

    public List<ShopAllotDetailForm> getShopAllotDetailListForNew(String fromShopId, String toShopId) {
        List<ShopAllotDetailDto> result = shopAllotDetailMapper.getShopAllotDetailListForNew( fromShopId, toShopId);
        cacheUtils.initCacheInput(result);

        return BeanUtil.map(result, ShopAllotDetailForm.class);
    }
}

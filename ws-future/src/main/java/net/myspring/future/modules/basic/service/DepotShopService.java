package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotShopService {
    @Autowired
    private DepotShopMapper depotShopMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotShopQuery){
        Page<DepotShopDto> page=depotShopMapper.findPage(pageable,depotShopQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}

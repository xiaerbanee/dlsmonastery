package net.myspring.future.modules.basic.service;

import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotShopMapper;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotShopService {
    @Autowired
    private DepotShopMapper depotShopMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotShopDto> findPage(Pageable pageable,DepotShopQuery depotShopQuery){
        Page<DepotShopDto> page=depotShopMapper.findPage(pageable,depotShopQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DepotDto> findDepotDtoList(DepotShopQuery depotShopQuery) {
        depotShopQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        depotShopQuery.setDepotIdList(depotManager.filterDepotIds());
        return depotShopMapper.findDepotDtoList(depotShopQuery);
    }


}

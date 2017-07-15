package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.repository.AfterSaleProductAllotRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by zhucc on 2017/7/4.
 */
@Service
@Transactional(readOnly=true)
public class AfterSaleProductAllotService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AfterSaleProductAllotRepository afterSaleProductAllotRepository;
    @Autowired
    private DepotManager depotManager;

    public Page<AfterSaleProductAllotDto> findPage(Pageable pageable, AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        afterSaleProductAllotQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<AfterSaleProductAllotDto> afterSaleProductAllotDtoPage=afterSaleProductAllotRepository.findPage(pageable,afterSaleProductAllotQuery);
        cacheUtils.initCacheInput(afterSaleProductAllotDtoPage.getContent());
        return afterSaleProductAllotDtoPage;
    }
}



package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.dto.AfterSaleStoreAllotDto;
import net.myspring.future.modules.crm.repository.AfterSaleProductAllotRepository;
import net.myspring.future.modules.crm.repository.AfterSaleStoreAllotRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleStoreAllotQuery;
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
public class AfterSaleStoreAllotService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AfterSaleStoreAllotRepository afterSaleStoreAllotRepository;

    public Page<AfterSaleStoreAllotDto> findPage(Pageable pageable, AfterSaleStoreAllotQuery afterSaleStoreAllotQuery){
        Page<AfterSaleStoreAllotDto> afterSaleStoreAllotDtoPage=afterSaleStoreAllotRepository.findPage(pageable,afterSaleStoreAllotQuery);
        cacheUtils.initCacheInput(afterSaleStoreAllotDtoPage.getContent());
        return afterSaleStoreAllotDtoPage;
    }
}

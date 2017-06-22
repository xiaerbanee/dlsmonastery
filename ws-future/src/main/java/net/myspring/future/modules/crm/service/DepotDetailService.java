package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.DepotDetailDto;
import net.myspring.future.modules.crm.repository.DepotDetailRepository;
import net.myspring.future.modules.crm.web.query.DepotDetailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotDetailService {

    @Autowired
    private DepotDetailRepository depotDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotDetailDto> findPage(Pageable pageable, DepotDetailQuery depotDetailQuery){

        Page<DepotDetailDto> page= depotDetailRepository.findPage(pageable,depotDetailQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

}

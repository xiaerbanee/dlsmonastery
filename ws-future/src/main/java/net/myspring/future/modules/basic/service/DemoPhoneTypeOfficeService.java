package net.myspring.future.modules.basic.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;
import net.myspring.future.modules.basic.repository.DemoPhoneTypeOfficeRepository;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DemoPhoneTypeOfficeService {

    @Autowired
    private DemoPhoneTypeOfficeRepository demoPhoneTypeOfficeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DemoPhoneTypeOfficeDto> findPage(Pageable pageable, DemoPhoneTypeOfficeQuery demoPhoneTypeOfficeQuery){
        Page<DemoPhoneTypeOfficeDto> page = demoPhoneTypeOfficeRepository.findPage(pageable,demoPhoneTypeOfficeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

}

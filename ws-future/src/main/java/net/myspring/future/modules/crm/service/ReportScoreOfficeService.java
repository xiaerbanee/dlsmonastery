package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto;
import net.myspring.future.modules.crm.repository.ReportScoreOfficeRepository;
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportScoreOfficeService {

    @Autowired
    private ReportScoreOfficeRepository reportScoreOfficeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ReportScoreOfficeDto> findPage(Pageable pageable, ReportScoreOfficeQuery reportScoreOfficeQuery){
        Page<ReportScoreOfficeDto> page= reportScoreOfficeRepository.findPage(pageable,reportScoreOfficeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}

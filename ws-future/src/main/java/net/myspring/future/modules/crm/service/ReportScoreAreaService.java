package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.ReportScoreAreaDto;
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto;
import net.myspring.future.modules.crm.repository.ReportScoreAreaRepository;
import net.myspring.future.modules.crm.repository.ReportScoreOfficeRepository;
import net.myspring.future.modules.crm.web.query.ReportScoreAreaQuery;
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportScoreAreaService {

    @Autowired
    private ReportScoreAreaRepository reportScoreAreaRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ReportScoreAreaDto> findPage(Pageable pageable, ReportScoreAreaQuery reportScoreAreaQuery){
        Page<ReportScoreAreaDto> page= reportScoreAreaRepository.findPage(pageable,reportScoreAreaQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}

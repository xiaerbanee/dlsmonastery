package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.repository.DutyRestRepository;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DutyRestService {

    @Autowired
    private DutyRestRepository dutyRestRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<DutyRestDto> findPage(Pageable pageable,DutyRestQuery dutyRestQuery) {
        Page<DutyRestDto> page = dutyRestRepository.findPage(pageable, dutyRestQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyRest save(DutyRestForm dutyRestForm) {
        dutyRestForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutyRestForm.setEmployeeId(RequestUtils.getEmployeeId());
        DutyRest dutyRest = BeanUtil.map(dutyRestForm, DutyRest.class);
        dutyRestRepository.save(dutyRest);
        return dutyRest;
    }

    public void logicDelete(String id) {
        dutyRestRepository.logicDelete(id);
    }

    public DutyRest findOne(String id) {
        DutyRest dutyRest = dutyRestRepository.findOne(id);
        return dutyRest;
    }

    public DutyRestForm getForm(DutyRestForm dutyRestForm) {
        if(!dutyRestForm.isCreate()){
            DutyRest dutyRest =findOne(dutyRestForm.getId());
            dutyRestForm= BeanUtil.map(dutyRest,DutyRestForm.class);
            cacheUtils.initCacheInput(dutyRestForm);
        }
        return dutyRestForm;
    }
}

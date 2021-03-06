package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyPublicFree;
import net.myspring.basic.modules.hr.dto.DutyPublicFreeDto;
import net.myspring.basic.modules.hr.repository.DutyPublicFreeRepository;
import net.myspring.basic.modules.hr.web.form.DutyPublicFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyPublicFreeQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DutyPublicFreeService {

    @Autowired
    private DutyPublicFreeRepository dutyPublicFreeRepository;
    @Autowired
    private CacheUtils cacheUtils;


    @Transactional
    public DutyPublicFree save(DutyPublicFreeForm dutyPublicFreeForm) {
        dutyPublicFreeForm.setEmployeeId(RequestUtils.getEmployeeId());
        dutyPublicFreeForm.setStatus(AuditTypeEnum.APPLY.getValue());
        DutyPublicFree dutyPublicFree = BeanUtil.map(dutyPublicFreeForm, DutyPublicFree.class);
        dutyPublicFreeRepository.save(dutyPublicFree);
        return dutyPublicFree;
    }

    public Page<DutyPublicFreeDto> findPage(Pageable pageable, DutyPublicFreeQuery dutyPublicFreeQuery) {
        Page<DutyPublicFreeDto> page = dutyPublicFreeRepository.findPage(pageable,dutyPublicFreeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void logicDelete(String id) {
        dutyPublicFreeRepository.logicDelete(id);
    }

    public DutyPublicFree findOne(String id) {
        DutyPublicFree dutyPublicFree = dutyPublicFreeRepository.findOne(id);
        return dutyPublicFree;
    }

    public DutyPublicFreeForm getForm(DutyPublicFreeForm dutyPublicFreeForm) {
        if(!dutyPublicFreeForm.isCreate()){
            DutyPublicFree dutyPublicFree =dutyPublicFreeRepository.findOne(dutyPublicFreeForm.getId());
            dutyPublicFreeForm= BeanUtil.map(dutyPublicFree,DutyPublicFreeForm.class);
            cacheUtils.initCacheInput(dutyPublicFree);
        }
        return dutyPublicFreeForm;
    }
}

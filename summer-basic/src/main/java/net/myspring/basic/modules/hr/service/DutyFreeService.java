package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyFree;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.repository.DutyFreeRepository;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DutyFreeService {

    @Autowired
    private DutyFreeRepository dutyFreeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DutyFreeDto> findPage(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        Page<DutyFreeDto> page = dutyFreeRepository.findPage(pageable, dutyFreeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyFree save(DutyFreeForm dutyFreeForm) {
        dutyFreeForm.setEmployeeId(RequestUtils.getEmployeeId());
        dutyFreeForm.setStatus(AuditTypeEnum.APPLY.getValue());
        DutyFree dutyFree = BeanUtil.map(dutyFreeForm, DutyFree.class);
        dutyFreeRepository.save(dutyFree);
        return dutyFree;
    }

    public DutyFree findOne(String id) {
        DutyFree dutyFree = dutyFreeRepository.findOne(id);
        return dutyFree;
    }

    public DutyFreeForm getForm(DutyFreeForm dutyFreeForm) {
        if(!dutyFreeForm.isCreate()){
            DutyFree dutyFree =dutyFreeRepository.findOne(dutyFreeForm.getId());
            dutyFreeForm = BeanUtil.map(dutyFree,DutyFreeForm.class);
            cacheUtils.initCacheInput(dutyFreeForm);
        }
        return dutyFreeForm;
    }

    public void logicDelete(String id) {
        dutyFreeRepository.logicDelete(id);
    }

    public List<DutyFree> findByDate(LocalDate freeDate, String employeeId) {
        return dutyFreeRepository.findByDate(freeDate, employeeId);
    }
}

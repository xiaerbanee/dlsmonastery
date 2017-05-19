package net.myspring.basic.modules.hr.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyTrip;
import net.myspring.basic.modules.hr.dto.DutyTripDto;
import net.myspring.basic.modules.hr.mapper.DutyTripMapper;
import net.myspring.basic.modules.hr.web.form.DutyTripForm;
import net.myspring.basic.modules.hr.web.query.DutyTripQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DutyTripService {

    @Autowired
    private DutyTripMapper dutyTripMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<DutyTripDto> findPage(Pageable pageable, DutyTripQuery dutyTripQuery) {
        Page<DutyTripDto> page = dutyTripMapper.findPage(pageable, dutyTripQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DutyTrip save(DutyTripForm dutyTripForm) {
        dutyTripForm.setStatus(AuditTypeEnum.APPLYING.toString());
        dutyTripForm.setEmployeeId(RequestUtils.getRequestEntity().getEmployeeId());
        DutyTrip dutyTrip = BeanUtil.map(dutyTripForm, DutyTrip.class);
        dutyTripMapper.save(dutyTrip);
        return dutyTrip;
    }

    public void logicDeleteOne(String id) {
        dutyTripMapper.logicDeleteOne(id);
    }

    public DutyTrip findOne(String id) {
        DutyTrip dutyTrip = dutyTripMapper.findOne(id);
        return dutyTrip;
    }

    public DutyTripForm getForm(DutyTripForm dutyTripForm) {
        DutyTrip dutyTrip =dutyTripMapper.findOne(dutyTripForm.getId());
        dutyTripForm= BeanUtil.map(dutyTrip,DutyTripForm.class);
        cacheUtils.initCacheInput(dutyTripForm);
        return dutyTripForm;
    }
}

package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
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
    @Autowired
    private SecurityUtils securityUtils;

    public Page<DutyTripDto> findPage(Pageable pageable, DutyTripQuery dutyTripQuery) {
        Page<DutyTrip> page = dutyTripMapper.findPage(pageable, dutyTripQuery);
        Page<DutyTripDto> dutyTripDtoPage= BeanUtil.map(page,DutyTripDto.class);
        cacheUtils.initCacheInput(dutyTripDtoPage.getContent());
        return dutyTripDtoPage;
    }

    public DutyTripForm save(DutyTripForm dutyTripForm) {
        dutyTripForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutyTripForm.setEmployeeId(securityUtils.getEmployeeId());
        dutyTripMapper.saveForm(dutyTripForm);
        return dutyTripForm;
    }

    public void logicDeleteOne(String id) {
        dutyTripMapper.logicDeleteOne(id);
    }

    public DutyTrip findOne(String id) {
        DutyTrip dutyTrip = dutyTripMapper.findOne(id);
        return dutyTrip;
    }

    public DutyTripDto findDto(String id) {
        DutyTrip dutyTrip = findOne(id);
        DutyTripDto dutyTripDto= BeanUtil.map(dutyTrip,DutyTripDto.class);
        cacheUtils.initCacheInput(dutyTripDto);
        return dutyTripDto;
    }
}

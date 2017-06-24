package net.myspring.basic.modules.hr.service;


import com.google.common.collect.Lists;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyLeave;
import net.myspring.basic.modules.hr.dto.DutyLeaveDto;
import net.myspring.basic.modules.hr.repository.DutyLeaveRepository;
import net.myspring.basic.modules.hr.web.form.DutyLeaveForm;
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DutyLeaveService {

    @Autowired
    private DutyLeaveRepository dutyLeaveRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<DutyLeaveDto> findPage(Pageable pageable, DutyLeaveQuery dutyLeaveQuery) {
        Page<DutyLeaveDto> page = dutyLeaveRepository.findPage(pageable, dutyLeaveQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DutyLeave> save(DutyLeaveForm dutyLeaveForm) {
        List<DutyLeave> dutyLeaveList= Lists.newArrayList();
        if (dutyLeaveForm.getDutyDateStart().equals(dutyLeaveForm.getDutyDateEnd())) {
            if (dutyLeaveRepository.findByEmployeeAndDateAndDateType(dutyLeaveForm.getEmployeeId(), dutyLeaveForm.getDutyDateStart(), dutyLeaveForm.getDateType())  == null) {
                dutyLeaveForm.setDutyDate(dutyLeaveForm.getDutyDateStart());
                dutyLeaveForm.setStatus(AuditTypeEnum.APPLY.getValue());
                dutyLeaveForm.setEmployeeId(RequestUtils.getEmployeeId());
                DutyLeave dutyLeave=BeanUtil.map(dutyLeaveForm,DutyLeave.class);
                dutyLeaveRepository.save(dutyLeave);
                dutyLeave=dutyLeaveRepository.findOne(dutyLeave.getId());
                dutyLeaveList.add(dutyLeave);
            }
        }else {
            List<LocalDate> dateList = LocalDateUtils.getDateList(dutyLeaveForm.getDutyDateStart(), dutyLeaveForm.getDutyDateEnd());
            for (LocalDate date : dateList) {
                if (dutyLeaveRepository.findByEmployeeAndDateAndDateType(dutyLeaveForm.getEmployeeId(), date, DutyDateTypeEnum.全天.toString()) == null) {
                    DutyLeave item = new DutyLeave();
                    item.setDateType(DutyDateTypeEnum.全天.toString());
                    item.setDutyDate(date);
                    item.setStatus(AuditTypeEnum.APPLY.getValue());
                    item.setLeaveType(dutyLeaveForm.getLeaveType());
                    item.setAttachment(dutyLeaveForm.getAttachment());
                    item.setRemarks(dutyLeaveForm.getRemarks());
                    item.setEmployeeId(RequestUtils.getEmployeeId());
                    dutyLeaveRepository.save(item);
                    dutyLeaveList.add(item);
                }
            }
        }
        return dutyLeaveList;
    }

    public void logicDelete(String id) {
        dutyLeaveRepository.logicDelete(id);
    }

    public DutyLeave findOne(String id) {
        DutyLeave dutyLeave=dutyLeaveRepository.findOne(id);
        return dutyLeave;
    }

    public DutyLeaveForm getForm(DutyLeaveForm dutyLeaveForm) {
        if(!dutyLeaveForm.isCreate()){
            DutyLeave dutyLeave=dutyLeaveRepository.findOne(dutyLeaveForm.getId());
            dutyLeaveForm= BeanUtil.map(dutyLeave,DutyLeaveForm.class);
            cacheUtils.initCacheInput(dutyLeaveForm);
        }
        return dutyLeaveForm;
    }

    public List<DutyLeave> findByDutyDateList(String employeeId,List<LocalDate> dutyDateList){
        return dutyLeaveRepository.findByDutyDateList(employeeId,dutyDateList);
    }

    public List<DutyLeave>  findByDutyDate(String employeeId,LocalDate dutyDate){
        return dutyLeaveRepository.findByDutyDate(employeeId,dutyDate);
    }
}

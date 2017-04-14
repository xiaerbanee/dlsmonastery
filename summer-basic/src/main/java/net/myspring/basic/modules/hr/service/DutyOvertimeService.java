package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutyOvertime;
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto;
import net.myspring.basic.modules.hr.mapper.DutyOvertimeMapper;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.basic.modules.hr.web.query.DutyOvertimeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DutyOvertimeService {

    @Autowired
    private DutyOvertimeMapper dutyOvertimeMapper;
    @Autowired
    private CacheUtils cacheUtils;



    public Page<DutyOvertimeDto> findPage(Pageable pageable, DutyOvertimeQuery dutyOvertimeQuery) {
        Page<DutyOvertime> page = dutyOvertimeMapper.findPage(pageable, dutyOvertimeQuery);
        Page<DutyOvertimeDto> dutyOvertimeDtoPage= BeanUtil.map(page,DutyOvertimeDto.class);
        cacheUtils.initCacheInput(dutyOvertimeDtoPage.getContent());
        return dutyOvertimeDtoPage;
    }

    public DutyOvertimeForm save(DutyOvertimeForm dutyOvertimeForm) {
        dutyOvertimeForm.setLeftHour(dutyOvertimeForm.getHour());
        dutyOvertimeForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutyOvertimeForm.setEmployeeId(SecurityUtils.getEmployeeId());
        dutyOvertimeMapper.save(BeanUtil.map(dutyOvertimeForm,DutyOvertime.class));
        return dutyOvertimeForm;
    }

    public List<DutyOvertime> findByDutyDate(String employeeId, LocalDate dutyDate) {
        List<DutyOvertime> dutyOvertimes = dutyOvertimeMapper.findByDutyDate(employeeId, dutyDate);
        return dutyOvertimes;
    }

    public List<DutyOvertime> findByDutyDateAndStatus(String employeeId, LocalDate dutyDateStart, LocalDate dutyDateEnd, String status) {
        List<DutyOvertime> dutyOvertimes = dutyOvertimeMapper.findByDutyDateAndStatus(employeeId, dutyDateStart, dutyDateEnd, status);
        return dutyOvertimes;
    }

    public void logicDeleteOne(String id) {
        dutyOvertimeMapper.logicDeleteOne(id);
    }


    public DutyOvertime findOne(String id) {
        DutyOvertime dutyOvertime = dutyOvertimeMapper.findOne(id);
        return dutyOvertime;
    }

    public DutyOvertimeDto findDto(String id) {
        DutyOvertime dutyOvertime =findOne(id);
        DutyOvertimeDto dutyOvertimeDto= BeanUtil.map(dutyOvertime,DutyOvertimeDto.class);
        cacheUtils.initCacheInput(dutyOvertimeDto);
        return dutyOvertimeDto;
    }

    public Double getAvailableHour(String employeeId, LocalDateTime currentDate) {
        Double overtimeHour = 0.0;
        if (currentDate == null) {
            return overtimeHour;
        }
        LocalDateTime dateStart = LocalDateTimeUtils.getFirstDayOfMonth(currentDate.minusMonths(3));
        LocalDateTime dateEnd = currentDate;
        List<DutyOvertime> overtimeList = dutyOvertimeMapper.findByIdAndDate(employeeId, dateStart, dateEnd, AuditTypeEnum.PASS.getValue());
        if (CollectionUtil.isNotEmpty(overtimeList)) {
            for (DutyOvertime dutyOvertime : overtimeList) {
                overtimeHour = overtimeHour + dutyOvertime.getLeftHour();
            }
        }
        return overtimeHour;
    }

    public Double getExpiredHour(String employeeId, LocalDateTime currentDate){
        Double overtimeHour = 0.0;
        if (currentDate == null) {
            return overtimeHour;
        }
        LocalDateTime dateStart = LocalDateTimeUtils.getFirstDayOfMonth(currentDate.minusMonths(3));
        LocalDateTime dateEnd =  LocalDateTimeUtils.getLastDayOfMonth(currentDate.minusMonths(3));
        List<DutyOvertime> overtimeList = dutyOvertimeMapper.findByIdAndDate(employeeId, dateStart, dateEnd, AuditTypeEnum.PASS.getValue());
        if (CollectionUtil.isNotEmpty(overtimeList)) {
            for (DutyOvertime dutyOvertime : overtimeList) {
                overtimeHour = overtimeHour + dutyOvertime.getLeftHour();
            }
        }
        return overtimeHour;
    }
}

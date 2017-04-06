package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.*;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.basic.modules.hr.mapper.*;
import net.myspring.basic.modules.hr.model.CalendarEvent;
import net.myspring.basic.modules.hr.model.DutyModel;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.time.LocalTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/23.
 */
@Service
@Transactional
public class DutyService {

    @Autowired
    private DutyWorktimeMapper dutyWorktimeMapper;
    @Autowired
    private DutyLeaveMapper dutyLeaveMapper;
    @Autowired
    private DutyOvertimeMapper dutyOvertimeMapper;
    @Autowired
    private DutyRestMapper dutyRestMapper;
    @Autowired
    private DutySignMapper dutySignMapper;
    @Autowired
    private DutyRestOvertimeMapper dutyRestOvertimeMapper;
    @Autowired
    private DutyAnnualMapper dutyAnnualMapper;
    @Autowired
    private DutyFreeMapper dutyFreeMapper;
    @Autowired
    private DutyPublicFreeMapper dutyPublicFreeMapper;
    @Autowired
    private DutyTripMapper dutyTripMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public List<DutyModel> findByAuditable(String leaderId, String status, LocalDateTime dateStart) {
        List<DutyModel> leaveList = dutyLeaveMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> overtimeList = dutyOvertimeMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> restList = dutyRestMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> signList = dutySignMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> freeList = dutyFreeMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> publicFreeList = dutyPublicFreeMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> tripList = dutyTripMapper.findByAuditable(leaderId, status, dateStart);
        List<DutyModel> dutyModelList = Lists.newArrayList();
        dutyModelList.addAll(leaveList);
        dutyModelList.addAll(overtimeList);
        dutyModelList.addAll(restList);
        dutyModelList.addAll(signList);
        dutyModelList.addAll(freeList);
        dutyModelList.addAll(publicFreeList);
        dutyModelList.addAll(tripList);
        return dutyModelList;
    }

    public Object findDutyItem(String id, String dutyType) {
        Object item;
        if (DutyTypeEnum.请假.name().equals(dutyType)) {
            item = dutyLeaveMapper.findOne(id);
        } else if (DutyTypeEnum.免打卡.name().equals(dutyType)) {
            item = dutyFreeMapper.findOne(id);
        } else if (DutyTypeEnum.加班.name().equals(dutyType)) {
            item = dutyOvertimeMapper.findOne(id);
        } else if (DutyTypeEnum.公休.name().equals(dutyType)) {
            item = dutyPublicFreeMapper.findOne(id);
        } else if (DutyTypeEnum.出差.name().equals(dutyType)) {
            item = dutyTripMapper.findOne(id);
        } else if (DutyTypeEnum.签到.name().equals(dutyType)) {
            item = dutySignMapper.findOne(id);
        } else {
            item = dutyRestMapper.findOne(id);
        }
        return item;
    }

    public void audit(Map<String,String> map){
        for(Map.Entry<String,String> entry:map.entrySet()){
            audit(entry.getKey(),entry.getValue(),true,null);
        }
    }

    public void audit(String id, String dutyType, Boolean pass, String auditRemarks) {
        String auditBy = securityUtils.getAccountId();
        if (DutyTypeEnum.请假.toString().equals(dutyType)) {
            DutyLeave dutyLeave = dutyLeaveMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyLeave.getStatus())) {
                dutyLeave.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutyLeave.setAuditBy(auditBy);
                dutyLeave.setAuditDate(LocalDateTime.now());
                dutyLeave.setAuditRemarks(auditRemarks);
                dutyLeave.setLocked(true);
                dutyLeaveMapper.update(dutyLeave);
            }
        } else if (DutyTypeEnum.免打卡.toString().equals(dutyType)) {
            DutyFree dutyFree = dutyFreeMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyFree.getStatus())) {
                dutyFree.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutyFree.setAuditBy(auditBy);
                dutyFree.setAuditDate(LocalDateTime.now());
                dutyFree.setLocked(true);
                dutyFree.setAuditRemarks(auditRemarks);
                dutyFreeMapper.update(dutyFree);
            }
        } else if (DutyTypeEnum.公休.toString().equals(dutyType)) {
            DutyPublicFree dutyPublicFree = dutyPublicFreeMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyPublicFree.getStatus())) {
                dutyPublicFree.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutyPublicFree.setAuditBy(auditBy);
                dutyPublicFree.setAuditDate(LocalDateTime.now());
                dutyPublicFree.setLocked(true);
                dutyPublicFree.setAuditRemarks(auditRemarks);
                dutyPublicFreeMapper.update(dutyPublicFree);
            }
        } else if (DutyTypeEnum.出差.toString().equals(dutyType)) {
            DutyTrip dutyTrip = dutyTripMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyTrip.getStatus())) {
                dutyTrip.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutyTrip.setAuditBy(auditBy);
                dutyTrip.setAuditDate(LocalDateTime.now());
                dutyTrip.setLocked(true);
                dutyTrip.setAuditRemarks(auditRemarks);
                dutyTripMapper.update(dutyTrip);
            }
        } else if (DutyTypeEnum.加班.toString().equals(dutyType)) {
            DutyOvertime dutyOvertime = dutyOvertimeMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyOvertime.getStatus())) {
                dutyOvertime.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutyOvertime.setAuditBy(auditBy);
                dutyOvertime.setAuditDate(LocalDateTime.now());
                dutyOvertime.setAuditRemarks(auditRemarks);
                dutyOvertime.setLocked(true);
                dutyOvertimeMapper.update(dutyOvertime);
            }
        } else if (DutyTypeEnum.调休.toString().equals(dutyType)) {
            DutyRest dutyRest = dutyRestMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutyRest.getStatus())) {
                restAudit(dutyRest, auditBy, pass, auditRemarks);
            }
        } else if (DutyTypeEnum.签到.toString().equals(dutyType)) {
            DutySign dutySign = dutySignMapper.findOne(id);
            if (AuditTypeEnum.APPLY.getValue().equals(dutySign.getStatus())) {
                dutySign.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
                dutySign.setAuditBy(auditBy);
                dutySign.setAuditRemarks(auditRemarks);
                dutySign.setLocked(true);
                if (pass) {
                    DutyWorktime dutyWorktime = new DutyWorktime();
                    Account account = accountMapper.findOne(dutySign.getCreatedBy());
                    dutyWorktime.setEmployeeId(account.getEmployeeId());
                    dutyWorktime.setDutyDate(dutySign.getCreatedDate().toLocalDate());
                    dutyWorktime.setDutyTime(dutySign.getCreatedDate().toLocalTime());
                    dutyWorktime.setType(WorkTimeTypeEnum.外勤.toString());
                    dutyWorktimeMapper.save(dutyWorktime);
                    dutySign.setDutyWorktimeId(dutyWorktime.getId());
                }
                dutySignMapper.update(dutySign);
            }
        }
    }

    private Boolean restAudit(DutyRest dutyRest, String auditById, Boolean pass, String auditRemarks) {
        if (pass) {
            Double restHour = 0.0;
            if (DutyRestTypeEnum.加班调休.toString().equals(dutyRest.getType())) {
                LocalDate dateStart = LocalDate.now().minusMonths(3);
                LocalDate dateEnd = dutyRest.getDutyDate();
                List<DutyOvertime> overtimeList = dutyOvertimeMapper.findByDutyDateAndStatus(dutyRest.getEmployeeId(), dateStart, dateEnd, AuditTypeEnum.PASS.getValue());
                List<DutyRestOvertime> dutyRestOvertimes = Lists.newArrayList();
                restHour = dutyRest.getHour().doubleValue();
                Double overTime = 0.0;
                for (DutyOvertime dutyOvertime : overtimeList) {
                    overTime += dutyOvertime.getLeftHour();
                }
                if (overTime < restHour) {
                   return false;
                }
                for (DutyOvertime dutyOvertime : overtimeList) {
                    if (dutyOvertime.getLeftHour() > 0 && restHour > 0) {
                        if (dutyOvertime.getLeftHour() >= restHour) {
                            DutyRestOvertime dutyRestOvertime = new DutyRestOvertime();
                            dutyRestOvertime.setRestHour(restHour);
                            dutyRestOvertime.setRestId(dutyRest.getId());
                            dutyRestOvertime.setOvertimeId(dutyOvertime.getId());
                            dutyRestOvertimes.add(dutyRestOvertime);
                            dutyOvertime.setLeftHour(dutyOvertime.getLeftHour() - restHour);
                            dutyOvertime.setLocked(true);
                            dutyOvertimeMapper.update(dutyOvertime);
                            break;
                        } else if (dutyOvertime.getLeftHour() < restHour) {
                            DutyRestOvertime dutyRestOvertime = new DutyRestOvertime();
                            dutyRestOvertime.setRestHour(dutyOvertime.getLeftHour());
                            restHour = restHour - dutyOvertime.getLeftHour();
                            dutyRestOvertime.setRestId(dutyRest.getId());
                            dutyRestOvertime.setOvertimeId(dutyOvertime.getId());
                            dutyRestOvertimes.add(dutyRestOvertime);
                            dutyOvertime.setLeftHour(0.0);
                            dutyOvertime.setLocked(true);
                            dutyOvertimeMapper.update(dutyOvertime);
                        }
                    }
                }
               if(CollectionUtil.isNotEmpty(dutyRestOvertimes)){
                   dutyRestOvertimeMapper.batchSave(dutyRestOvertimes);
               }
            } else {
                restHour = DutyDateTypeEnum.全天.toString().equals(dutyRest.getDateType()) ? 8.0 : 4.0;
                DutyAnnual dutyAnnual = dutyAnnualMapper.findByEmployee(dutyRest.getEmployeeId());
                if(dutyAnnual==null||dutyAnnual.getLeftHour()<restHour){
                    return false;
                }
                dutyAnnual.setLeftHour(dutyAnnual.getLeftHour() - restHour);
                dutyAnnualMapper.save(dutyAnnual);
                dutyRest.setDutyAnnual(dutyAnnual);
            }
        }
        dutyRest.setStatus(pass ? AuditTypeEnum.PASS.getValue() : AuditTypeEnum.NOT_PASS.getValue());
        dutyRest.setAuditBy(auditById);
        dutyRest.setAuditDate(LocalDateTime.now());
        dutyRest.setAuditRemarks(auditRemarks);
        dutyRest.setLocked(true);
        dutyRestMapper.update(dutyRest);
        return true;
    }

    public List<CalendarEvent> findEvent(String employeeId, LocalDate start, LocalDate end) {
        List<CalendarEvent> list = Lists.newArrayList();
        List<DutyWorktime> worktimeList = dutyWorktimeMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyLeave> leaveList = dutyLeaveMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyOvertime> overtimeList = dutyOvertimeMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyRest> restList = dutyRestMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyFree> freeList = dutyFreeMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyPublicFree> publicFreeList = dutyPublicFreeMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutyTrip> tripList = dutyTripMapper.findByEmployeeAndDate(employeeId, start, end);
        List<DutySign>signList=dutySignMapper.findByEmployeeAndDate(employeeId, start, end);
        for (DutyWorktime dutyWorktime : worktimeList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setId(dutyWorktime.getId());
            calendarEvent.setStart(dutyWorktime.getDutyDate());
            calendarEvent.setTitle("卡:" + LocalTimeUtils.format(dutyWorktime.getDutyTime()));
            calendarEvent.setCssClass(getCssClass(AuditTypeEnum.APPLY.getValue()));
            list.add(calendarEvent);
        }
        for (DutyLeave dutyLeave : leaveList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutyLeave.getDutyDate());
            calendarEvent.setTitle("假:" + dutyLeave.getDateType());
            calendarEvent.setContent("请假申请<br/>状态：" + dutyLeave.getStatus());
            calendarEvent.setCssClass(getCssClass(dutyLeave.getStatus()));
            list.add(calendarEvent);
        }
        for (DutyOvertime dutyOvertime : overtimeList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutyOvertime.getDutyDate());
            calendarEvent.setTitle("加:" + LocalTimeUtils.format(dutyOvertime.getTimeStart()) + "~" + LocalTimeUtils.format(dutyOvertime.getTimeEnd()));
            calendarEvent.setContent("加班申请<br/>状态：" + dutyOvertime.getStatus());
            calendarEvent.setCssClass(getCssClass(dutyOvertime.getStatus()));
            list.add(calendarEvent);
        }
        for (DutyRest dutyRest : restList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutyRest.getDutyDate());
            calendarEvent.setTitle("调:" + LocalTimeUtils.format(dutyRest.getTimeStart()) + "~" + LocalTimeUtils.format(dutyRest.getTimeEnd()));
            calendarEvent.setContent("调休申请<br/>状态：" + dutyRest.getStatus());
            calendarEvent.setCssClass(getCssClass(dutyRest.getStatus()));
            list.add(calendarEvent);
        }
        for (DutyFree dutyFree : freeList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutyFree.getFreeDate());
            calendarEvent.setTitle("免:" + dutyFree.getDateType());
            calendarEvent.setContent("免打卡申请<br/>状态：" + dutyFree.getStatus());
            calendarEvent.setCssClass(getCssClass(dutyFree.getStatus()));
            list.add(calendarEvent);
        }
        for (DutyPublicFree dutyPublicFree : publicFreeList) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutyPublicFree.getFreeDate());
            calendarEvent.setTitle("公:" + dutyPublicFree.getDateType());
            calendarEvent.setContent("公休申请<br/>状态：" + dutyPublicFree.getStatus());
            calendarEvent.setCssClass(getCssClass(dutyPublicFree.getStatus()));
            list.add(calendarEvent);
        }
        for (DutyTrip dutyTrip : tripList) {
            for(LocalDate date : LocalDateUtils.getDateList(dutyTrip.getDateStart(), dutyTrip.getDateEnd())){
                CalendarEvent calendarEvent = new CalendarEvent();
                calendarEvent.setStart(date);
                calendarEvent.setTitle("差:" + LocalDateUtils.format(date));
                calendarEvent.setContent("出差申请<br/>状态：" + dutyTrip.getStatus());
                calendarEvent.setCssClass(getCssClass(dutyTrip.getStatus()));
                list.add(calendarEvent);
            }
        }
        //签到
        for(DutySign dutySign:signList){
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setStart(dutySign.getDutyDate());
            calendarEvent.setTitle("签:" + LocalTimeUtils.format(dutySign.getDutyTime()));
            calendarEvent.setContent("签到申请<br/>状态：" + dutySign.getStatus());
            list.add(calendarEvent);
        }
        return list;
    }


    private String getCssClass(String status) {
        if (AuditTypeEnum.APPLY.getValue().equals(status)) {
            return "warning";
        } else if (AuditTypeEnum.PASS.getValue().equals(status)) {
            return "info";
        } else {
            return "danger";
        }
    }
}

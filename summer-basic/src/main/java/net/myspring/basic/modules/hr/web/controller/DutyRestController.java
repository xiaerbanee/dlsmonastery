package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.service.DutyRestService;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.basic.modules.hr.web.validator.DutyRestValidator;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyRest")
public class DutyRestController {

    @Autowired
    private DutyRestService dutyRestService;
    @Autowired
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private DutyOvertimeService dutyOvertimeService;
    @Autowired
    private DutyRestValidator dutyRestValidator;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyRestDto> list(Pageable pageable, DutyRestQuery dutyRestQuery) {
        dutyRestQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyRestDto> page = dutyRestService.findPage(pageable,dutyRestQuery);
        for(DutyRestDto dutyRestDto:page.getContent()){
            setOperationStatus(dutyRestDto);
        }
        return page;
    }

    @RequestMapping(value = "getForm")
    public DutyRestForm getForm(DutyRestForm dutyRestForm) {
        dutyRestForm.setOvertimeLeftHour(dutyOvertimeService.getAvailableHour(RequestUtils.getEmployeeId(), LocalDate.now()));
        dutyRestForm.setAnnualLeftHour(dutyAnnualService.getAvailableHour(RequestUtils.getEmployeeId()));
        dutyRestForm.setExpiredHour(dutyOvertimeService.getExpiredHour(RequestUtils.getEmployeeId(), LocalDate.now()));
        dutyRestForm.getExtra().put("restList", DutyRestTypeEnum.values());
        dutyRestForm.getExtra().put("dateList", DutyDateTypeEnum.values());
        return dutyRestForm;
    }

    @RequestMapping(value = "getQuery")
    public DutyRestQuery getQuery(DutyRestQuery dutyRestQuery) {
        dutyRestQuery.getExtra().put("restList", DutyRestTypeEnum.getList());
        dutyRestQuery.getExtra().put("dateList", DutyDateTypeEnum.getList());
        return dutyRestQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyRestForm dutyRestForm, BindingResult bindingResult) {
        dutyRestValidator.validate(dutyRestForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutyRestService.save(dutyRestForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyRestService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    private void setOperationStatus(DutyRestDto dutyRestDto){
        if(dutyRestDto.getCreatedBy().equals(RequestUtils.getAccountId())&& AuditTypeEnum.APPLY.getValue().equals(dutyRestDto.getStatus())){
            dutyRestDto.setDeleted(true);
        }
    }
}

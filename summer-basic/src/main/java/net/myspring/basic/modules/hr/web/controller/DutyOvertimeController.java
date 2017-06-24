package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.basic.modules.hr.web.query.DutyOvertimeQuery;
import net.myspring.basic.modules.hr.web.validator.DutyOvertimeValidator;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/dutyOvertime")
public class DutyOvertimeController {

    @Autowired
    private DutyOvertimeService dutyOvertimeService;
    @Autowired
    private DutyOvertimeValidator dutyOvertimeValidator;

    @RequestMapping(value = "getQuery")
    public DutyOvertimeQuery getQuery(DutyOvertimeQuery dutyOvertimeQuery) {
        return dutyOvertimeQuery;
    }


    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyOvertimeDto> list(Pageable pageable, DutyOvertimeQuery dutyOvertimeQuery) {
        dutyOvertimeQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyOvertimeDto> page = dutyOvertimeService.findPage(pageable,dutyOvertimeQuery);
        for(DutyOvertimeDto dutyOvertimeDto:page.getContent()){
            setOperationStatus(dutyOvertimeDto);
        }
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyOvertimeForm dutyOvertimeForm, BindingResult bindingResult) {
        dutyOvertimeValidator.validate(dutyOvertimeForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutyOvertimeService.save(dutyOvertimeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyOvertimeService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    private void setOperationStatus(DutyOvertimeDto dutyOvertimeDto){
        if(dutyOvertimeDto.getCreatedBy().equals(RequestUtils.getAccountId()) && AuditTypeEnum.APPLY.getValue().equals(dutyOvertimeDto.getStatus())){
            dutyOvertimeDto.setDeleted(true);
        }
    }
}

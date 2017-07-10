package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.dto.DutyTripDto;
import net.myspring.basic.modules.hr.service.DutyTripService;
import net.myspring.basic.modules.hr.web.form.DutyTripForm;
import net.myspring.basic.modules.hr.web.query.DutyTripQuery;
import net.myspring.basic.modules.hr.web.validator.DutyTripValidator;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/dutyTrip")
public class DutyTripController {
    @Autowired
    private DutyTripService dutyTripService;
    @Autowired
    private DutyTripValidator dutyTripValidator;

    @RequestMapping(value = "getQuery")
    public DutyTripQuery getQuery(DutyTripQuery dutyTripQuery) {
        return dutyTripQuery;
    }


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:dutyTrip:view')")
    public Page<DutyTripDto>  list(Pageable pageable, DutyTripQuery dutyTripQuery){
        dutyTripQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyTripDto> page = dutyTripService.findPage(pageable,dutyTripQuery);
        for(DutyTripDto dutyTripDto:page.getContent()){
            setOperationStatus(dutyTripDto);
        }
        return page;
    }

    @RequestMapping(value="save")
    @PreAuthorize("hasPermission(null,'hr:dutyTrip:edit')")
    public RestResponse save(DutyTripForm dutyTripForm, BindingResult bindingResult){
        dutyTripValidator.validate(dutyTripForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutyTripService.save(dutyTripForm);
        return  new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'hr:dutyTrip:delete')")
    public RestResponse delete(String id) {
        dutyTripService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    private void setOperationStatus(DutyTripDto dutyTripDto){
        if(dutyTripDto.getCreatedBy().equals(RequestUtils.getAccountId())&& AuditTypeEnum.APPLY.getValue().equals(dutyTripDto.getStatus())){
            dutyTripDto.setDeleted(true);
        }
    }

}

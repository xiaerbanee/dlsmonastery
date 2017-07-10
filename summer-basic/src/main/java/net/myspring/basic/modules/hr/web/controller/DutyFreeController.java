package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.service.DutyFreeService;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.basic.modules.hr.web.validator.DutyFreeValidator;
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
@RequestMapping(value = "hr/dutyFree")
public class DutyFreeController {

    @Autowired
    private DutyFreeService dutyFreeService;
    @Autowired
    private DutyFreeValidator dutyFreeValidator;

    @RequestMapping(value = "getQuery")
    public DutyFreeQuery getQuery(DutyFreeQuery dutyFreeQuery) {
        return dutyFreeQuery;
    }


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:dutyFree:view')")
    public Page<DutyFreeDto> list(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        dutyFreeQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyFreeDto> page = dutyFreeService.findPage(pageable, dutyFreeQuery);
        for(DutyFreeDto dutyFreeDto:page.getContent()){
            setOperationStatus(dutyFreeDto);
        }
        return page;
    }

    @RequestMapping(value="findOne")
    public DutyFreeForm getForm(DutyFreeForm dutyFreeForm){
        dutyFreeForm.getExtra().put("dateList",DutyDateTypeEnum.getList());
        return dutyFreeForm;
    }


    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:dutyFree:delete')")
    public RestResponse delete(String id) {
        dutyFreeService.logicDelete(id);
        RestResponse restResponse = new RestResponse("免打卡删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'hr:dutyFree:edit')")
    public RestResponse save(DutyFreeForm dutyFreeForm, BindingResult bindingResult) {
        dutyFreeValidator.validate(dutyFreeForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutyFreeService.save(dutyFreeForm);
        return new RestResponse("保存成功",null);
    }

    private void setOperationStatus(DutyFreeDto dutyFreeDto){
        if(dutyFreeDto.getCreatedBy().equals(RequestUtils.getAccountId())&& AuditTypeEnum.APPLY.getValue().equals(dutyFreeDto.getStatus())){
            dutyFreeDto.setDeleted(true);
        }
    }
}

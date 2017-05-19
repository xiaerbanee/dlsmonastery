package net.myspring.basic.modules.hr.web.controller;


import net.myspring.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyLeaveDto;
import net.myspring.basic.modules.hr.service.DutyLeaveService;
import net.myspring.basic.modules.hr.web.form.DutyLeaveForm;
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/dutyLeave")
public class DutyLeaveController {

    @Autowired
    private DutyLeaveService dutyLeaveService;
    @Autowired
    private DictEnumService dictEnumService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyLeaveDto> list(Pageable pageable, DutyLeaveQuery dutyLeaveQuery){
        dutyLeaveQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyLeaveDto> page = dutyLeaveService.findPage(pageable,dutyLeaveQuery);
        return page;
    }

    @RequestMapping(value="findOne")
    public DutyLeaveForm getFormProperty(DutyLeaveForm dutyLeaveForm){
        dutyLeaveForm.setDateList(DutyDateTypeEnum.values());
        dutyLeaveForm.setLeaveList(dictEnumService.findValueByCategory(DictEnumCategoryEnum.DUTY_LEAVE_TYPE.toString()));
        return dutyLeaveForm;
    }

    @RequestMapping(value="getQuery")
    public DutyLeaveQuery getQuery(DutyLeaveQuery dutyLeaveQuery){
        dutyLeaveQuery.setDateList( DutyDateTypeEnum.getList());
        dutyLeaveQuery.setLeaveList(dictEnumService.findValueByCategory(DictEnumCategoryEnum.DUTY_LEAVE_TYPE.toString()));
        return dutyLeaveQuery;
    }

    @RequestMapping(value="save")
    public RestResponse save(DutyLeaveForm dutyLeaveForm, BindingResult  bindingResult){
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyLeaveService.save(dutyLeaveForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyLeaveService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

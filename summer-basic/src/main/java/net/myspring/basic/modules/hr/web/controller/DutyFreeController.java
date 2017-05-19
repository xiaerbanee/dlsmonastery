package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.service.DutyFreeService;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/dutyFree")
public class DutyFreeController {

    @Autowired
    private DutyFreeService dutyFreeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyFreeDto> list(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        dutyFreeQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyFreeDto> page = dutyFreeService.findPage(pageable, dutyFreeQuery);
        return page;
    }

    @RequestMapping(value="findOne")
    public DutyFreeForm getForm(DutyFreeForm dutyFreeForm){
        dutyFreeForm.setDateList(DutyDateTypeEnum.getList());
        return dutyFreeForm;
    }


    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(String id) {
        dutyFreeService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("免打卡删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyFreeForm dutyFreeForm, BindingResult bindingResult) {
        dutyFreeService.save(dutyFreeForm);
        RestResponse restResponse = new RestResponse("申请成功",null);
        return restResponse;
    }
}

package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.basic.modules.hr.web.query.DutyOvertimeQuery;
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

    @RequestMapping(value = "getQuery")
    public DutyOvertimeQuery getQuery(DutyOvertimeQuery dutyOvertimeQuery) {
        return dutyOvertimeQuery;
    }


    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyOvertimeDto> list(Pageable pageable, DutyOvertimeQuery dutyOvertimeQuery) {
        dutyOvertimeQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyOvertimeDto> page = dutyOvertimeService.findPage(pageable,dutyOvertimeQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyOvertimeForm dutyOvertimeForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyOvertimeService.save(dutyOvertimeForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyOvertimeService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

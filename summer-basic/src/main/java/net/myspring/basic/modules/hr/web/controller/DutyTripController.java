package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyTripDto;
import net.myspring.basic.modules.hr.service.DutyTripService;
import net.myspring.basic.modules.hr.web.form.DutyTripForm;
import net.myspring.basic.modules.hr.web.query.DutyTripQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "hr/dutyTrip")
public class DutyTripController {
    @Autowired
    private DutyTripService dutyTripService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyTripDto>  list(Pageable pageable, DutyTripQuery dutyTripQuery){
        dutyTripQuery.setCreatedBy(SecurityUtils.getAccountId());
        Page<DutyTripDto> page = dutyTripService.findPage(pageable,dutyTripQuery);
        return page;
    }

    @RequestMapping(value="save")
    public RestResponse save(DutyTripForm dutyTripForm, BindingResult bindingResult){
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyTripService.save(dutyTripForm);
        return  restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyTripService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

}

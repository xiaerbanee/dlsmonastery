package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyTripDto;
import net.myspring.basic.modules.hr.service.DutyTripService;
import net.myspring.basic.modules.hr.web.form.DutyTripForm;
import net.myspring.basic.modules.hr.web.query.DutyTripQuery;
import net.myspring.basic.modules.hr.web.validator.DutyTripValidator;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<DutyTripDto>  list(Pageable pageable, DutyTripQuery dutyTripQuery){
        dutyTripQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyTripDto> page = dutyTripService.findPage(pageable,dutyTripQuery);
        return page;
    }

    @RequestMapping(value="save")
    public RestResponse save(DutyTripForm dutyTripForm, BindingResult bindingResult){
        dutyTripValidator.validate(dutyTripForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        dutyTripService.save(dutyTripForm);
        return  new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyTripService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

}

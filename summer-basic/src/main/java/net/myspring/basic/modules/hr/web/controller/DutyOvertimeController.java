package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyOvertimeDto;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.web.form.DutyOvertimeForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "hr/dutyOvertime")
public class DutyOvertimeController {

    @Autowired
    private DutyOvertimeService dutyOvertimeService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().put("createdBy", securityUtils.getAccountId());
        Page<DutyOvertimeDto> page = dutyOvertimeService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "save")
    public String save(DutyOvertimeForm dutyOvertimeForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyOvertimeService.save(dutyOvertimeForm);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        dutyOvertimeService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }
}

package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.service.DutyRestService;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyRest")
public class DutyRestController {

    @Autowired
    private DutyRestService dutyRestService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private DutyOvertimeService dutyOvertimeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().put("createdBy", securityUtils.getAccountId());
        Page<DutyRestDto> page = dutyRestService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(DutyRestForm dutyRestForm) {
        Map<String, Object> map = Maps.newHashMap();
        dutyRestForm.setOvertimeLeftHour(dutyOvertimeService.getAvailableHour(securityUtils.getEmployeeId(), LocalDateTime.now()));
        dutyRestForm.setAnnualLeftHour(dutyAnnualService.getAvailableHour(securityUtils.getEmployeeId()));
        map.put("dutyRest", dutyRestForm);
        map.put("expiredHour", dutyOvertimeService.getExpiredHour(securityUtils.getEmployeeId(), LocalDateTime.now()));
        map.put("restList", DutyRestTypeEnum.values());
        map.put("dateList", DutyDateTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("restList", DutyRestTypeEnum.values());
        map.put("dateList", DutyDateTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(DutyRestForm dutyRestForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyRestService.save(dutyRestForm);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        dutyRestService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }
}

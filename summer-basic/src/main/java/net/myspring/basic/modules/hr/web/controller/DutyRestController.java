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
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private DutyOvertimeService dutyOvertimeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyRestDto> list(Pageable pageable, DutyRestQuery dutyRestQuery) {
        dutyRestQuery.setCreatedBy(SecurityUtils.getAccountId());
        Page<DutyRestDto> page = dutyRestService.findPage(pageable,dutyRestQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public Map<String, Object> getFormProperty(DutyRestForm dutyRestForm) {
        Map<String, Object> map = Maps.newHashMap();
        dutyRestForm.setOvertimeLeftHour(dutyOvertimeService.getAvailableHour(SecurityUtils.getEmployeeId(), LocalDateTime.now()));
        dutyRestForm.setAnnualLeftHour(dutyAnnualService.getAvailableHour(SecurityUtils.getEmployeeId()));
        map.put("dutyRestForm", dutyRestForm);
        map.put("expiredHour", dutyOvertimeService.getExpiredHour(SecurityUtils.getEmployeeId(), LocalDateTime.now()));
        map.put("restList", DutyRestTypeEnum.values());
        map.put("dateList", DutyDateTypeEnum.values());
        return map;
    }

    @RequestMapping(value = "getQuery")
    public Map<String, Object> getQuery(DutyRestQuery dutyRestQuery) {
        Map<String, Object> map = Maps.newHashMap();
        dutyRestQuery.setRestList(DutyRestTypeEnum.getList());
        dutyRestQuery.setDateList(DutyDateTypeEnum.getList());
        return map;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyRestForm dutyRestForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyRestService.save(dutyRestForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyRestService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

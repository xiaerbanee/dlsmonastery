package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.enums.DutyRestTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyRestDto;
import net.myspring.basic.modules.hr.service.DutyAnnualService;
import net.myspring.basic.modules.hr.service.DutyOvertimeService;
import net.myspring.basic.modules.hr.service.DutyRestService;
import net.myspring.basic.modules.hr.web.form.DutyRestForm;
import net.myspring.basic.modules.hr.web.query.DutyRestQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        dutyRestQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyRestDto> page = dutyRestService.findPage(pageable,dutyRestQuery);
        return page;
    }

    @RequestMapping(value = "getForm")
    public Map<String, Object> getForm(DutyRestForm dutyRestForm) {
        Map<String, Object> map = Maps.newHashMap();
        dutyRestForm.setOvertimeLeftHour(dutyOvertimeService.getAvailableHour(RequestUtils.getRequestEntity().getEmployeeId(), LocalDate.now()));
        dutyRestForm.setAnnualLeftHour(dutyAnnualService.getAvailableHour(RequestUtils.getRequestEntity().getEmployeeId()));
        map.put("dutyRestForm", dutyRestForm);
        map.put("expiredHour", dutyOvertimeService.getExpiredHour(RequestUtils.getRequestEntity().getEmployeeId(), LocalDate.now()));
        map.put("restList", DutyRestTypeEnum.values());
        map.put("dateList", DutyDateTypeEnum.values());
        return map;
    }

    @RequestMapping(value = "getQuery")
    public DutyRestQuery getQuery(DutyRestQuery dutyRestQuery) {
        dutyRestQuery.getExtra().put("restList", DutyRestTypeEnum.getList());
        dutyRestQuery.getExtra().put("dateList", DutyDateTypeEnum.getList());
        return dutyRestQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyRestForm dutyRestForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyRestService.save(dutyRestForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyRestService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

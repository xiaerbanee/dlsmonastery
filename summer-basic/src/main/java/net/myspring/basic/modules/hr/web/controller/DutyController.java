package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.model.CalendarEvent;
import net.myspring.basic.modules.hr.model.DutyModel;
import net.myspring.basic.modules.hr.service.DutyService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/23.
 */
@RestController
@RequestMapping(value = "hr/duty")
public class DutyController {

    @Autowired
    private DutyService dutyService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return ObjectMapperUtils.writeValueAsString(getDutyModelList());
    }

    @RequestMapping(value = "detail")
    public String detail(String id, String dutyType) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("item", dutyService.findDutyItem(id, dutyType));
        map.put("dutyType", dutyType);
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "audit")
    public RestResponse audit(String id, String dutyType, Boolean pass, String auditRemarks) {
        dutyService.audit(id, dutyType, pass, auditRemarks);
        return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "passAll")
    public RestResponse passAll() {
        List<DutyModel> dutyModelList = getDutyModelList();
        for(DutyModel dutyModel : dutyModelList) {
            dutyService.audit(dutyModel.getId(), dutyModel.getDutyType(),true,null);
        }
        return new RestResponse("审批成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "batchPass")
    public RestResponse batchPass(String dutyAuditMap){
        dutyAuditMap= HtmlUtils.htmlUnescape(dutyAuditMap);
        Map<String,String> map = ObjectMapperUtils.readValue(dutyAuditMap,Map.class);
        dutyService.audit(map);
        return new RestResponse("审批成功",ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "events", method = RequestMethod.GET)
    public String events(String start, String end) {
        LocalDate dateStart= LocalDateUtils.parse(start);
        LocalDate dateEnd= LocalDateUtils.parse(end);
        List<CalendarEvent> events = dutyService.findEvent(securityUtils.getEmployeeId(),dateStart,dateEnd);
        return ObjectMapperUtils.writeValueAsString(events);
    }

    private List<DutyModel> getDutyModelList() {
        LocalDateTime dateStart = LocalDateTime.now().minusMonths(1);
        String leaderId = securityUtils.getAccountId();
        List<DutyModel> list = dutyService.findByAuditable(leaderId, AuditTypeEnum.APPLY.getValue(), dateStart);
        return list;
    }
}

package net.myspring.basic.modules.hr.web.controller;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.enums.BoolEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.CalendarEventDto;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.service.DutyService;
import net.myspring.basic.modules.hr.web.form.DutyForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
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


    @RequestMapping(method = RequestMethod.GET)
    public List<DutyDto> list() {
        return getDutyDtoList();
    }

    @RequestMapping(value = "getForm")
    public DutyForm detail(DutyForm dutyForm) {
        dutyForm.setItem(dutyService.findDutyItem(dutyForm.getId(), dutyForm.getDutyType()));
        dutyForm.setBoolMap(BoolEnum.getMap());
        return dutyForm;
    }


    @RequestMapping(value = "audit")
    public RestResponse audit(String id, String dutyType, Boolean pass, String auditRemarks) {
        StringBuilder stringBuilder=dutyService.audit(id, dutyType, pass, auditRemarks);
        if(StringUtils.isNotBlank(stringBuilder.toString())){
            return new RestResponse(stringBuilder.toString(), ResponseCodeEnum.audited.name(),false);
        }else {
            return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "passAll")
    public RestResponse passAll() {
        List<DutyDto> dutyDtoList = getDutyDtoList();
        StringBuilder stringBuilder=new StringBuilder();
        for(DutyDto dutyDto : dutyDtoList) {
            stringBuilder.append(dutyService.audit(dutyDto.getId(), dutyDto.getDutyType(),true,null));
        }
        if(StringUtils.isNotBlank(stringBuilder.toString())){
            return new RestResponse(stringBuilder.toString(), ResponseCodeEnum.audited.name(),false);
        }else {
            return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "batchPass")
    public RestResponse batchPass(String dutyAuditMap){
        dutyAuditMap= HtmlUtils.htmlUnescape(dutyAuditMap);
        Map<String,String> map = ObjectMapperUtils.readValue(dutyAuditMap,Map.class);
        StringBuilder stringBuilder=new StringBuilder(dutyService.audit(map));
        if(StringUtils.isNotBlank(stringBuilder.toString())){
            return new RestResponse(stringBuilder.toString(), ResponseCodeEnum.audited.name(),false);
        }else {
            return new RestResponse("审核成功", ResponseCodeEnum.audited.name());
        }
    }

    @RequestMapping(value = "events", method = RequestMethod.GET)
    public List<CalendarEventDto> events(String start, String end) {
        LocalDate dateStart= LocalDateUtils.parse(start);
        LocalDate dateEnd= LocalDateUtils.parse(end);
        List<CalendarEventDto> events = dutyService.findEvent(RequestUtils.getEmployeeId(),dateStart,dateEnd);
        return events;
    }

    private List<DutyDto> getDutyDtoList() {
        LocalDateTime dateStart = LocalDateTime.now().minusMonths(1);
        String leaderId = RequestUtils.getAccountId();
        List<DutyDto> list = dutyService.findByAuditable(leaderId, AuditTypeEnum.APPLY.getValue(), dateStart);
        return list;
    }
}

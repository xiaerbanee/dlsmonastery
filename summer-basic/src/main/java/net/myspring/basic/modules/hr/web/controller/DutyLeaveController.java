package net.myspring.basic.modules.hr.web.controller;


import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyLeaveDto;
import net.myspring.basic.modules.hr.service.DutyLeaveService;
import net.myspring.basic.modules.hr.web.form.DutyLeaveForm;
import net.myspring.basic.modules.hr.web.query.DutyLeaveQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyLeave")
public class DutyLeaveController {

    @Autowired
    private DutyLeaveService dutyLeaveService;
    @Autowired
    private DictEnumService dictEnumService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, DutyLeaveQuery dutyLeaveQuery){
        dutyLeaveQuery.setCreatedBy(securityUtils.getAccountId());
        Page<DutyLeaveDto> page = dutyLeaveService.findPage(pageable,dutyLeaveQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map=Maps.newHashMap();
        map.put("dateList", DutyDateTypeEnum.values());
        map.put("leaveList",dictEnumService.findValueByCategory(DictEnumCategoryEnum.DUTY_LEAVE_TYPE.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("dateList", DutyDateTypeEnum.values());
        map.put("leaveList",dictEnumService.findValueByCategory(DictEnumCategoryEnum.DUTY_LEAVE_TYPE.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="save")
    public String save(DutyLeaveForm dutyLeaveForm, BindingResult  bindingResult){
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyLeaveService.save(dutyLeaveForm);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        dutyLeaveService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }
}

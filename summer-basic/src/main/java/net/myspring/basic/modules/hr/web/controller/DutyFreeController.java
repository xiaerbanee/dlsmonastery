package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.service.DutyFreeService;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "hr/dutyFree")
public class DutyFreeController {

    @Autowired
    private DutyFreeService dutyFreeService;
    @Autowired
    private SecurityUtils securityUtils;


    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyFreeDto> list(Pageable pageable, DutyFreeQuery dutyFreeQuery) {
        dutyFreeQuery.setCreatedBy(securityUtils.getAccountId());
        Page<DutyFreeDto> page = dutyFreeService.findPage(pageable, dutyFreeQuery);
        return page;
    }

    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("dateList", DutyDateTypeEnum.values());
        return map;
    }


    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(String id) {
        dutyFreeService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("免打卡删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyFreeForm dutyFreeForm, BindingResult bindingResult) {
        dutyFreeService.save(dutyFreeForm);
        RestResponse restResponse = new RestResponse("申请成功",null);
        return restResponse;
    }
}

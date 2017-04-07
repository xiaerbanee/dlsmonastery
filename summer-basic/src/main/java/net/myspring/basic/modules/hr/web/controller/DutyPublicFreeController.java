package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.DutyPublicFreeDto;
import net.myspring.basic.modules.hr.service.DutyPublicFreeService;
import net.myspring.basic.modules.hr.web.form.DutyPublicFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyPublicFreeQuery;
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
@RequestMapping(value = "hr/dutyPublicFree")
public class DutyPublicFreeController {

    @Autowired
    private DutyPublicFreeService dutyPublicFreeService;
    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, DutyPublicFreeQuery dutyPublicFreeQuery) {
        dutyPublicFreeQuery.setCreatedBy(securityUtils.getAccountId());
        Page<DutyPublicFreeDto> page = dutyPublicFreeService.findPage(pageable,dutyPublicFreeQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("dateList", DutyDateTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(DutyPublicFreeForm dutyPublicFreeForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyPublicFreeService.save(dutyPublicFreeForm);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        dutyPublicFreeService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }
}

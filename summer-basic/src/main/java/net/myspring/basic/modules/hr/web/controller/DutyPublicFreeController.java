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

    @RequestMapping(method = RequestMethod.GET)
    public Page<DutyPublicFreeDto> list(Pageable pageable, DutyPublicFreeQuery dutyPublicFreeQuery) {
        dutyPublicFreeQuery.setCreatedBy(SecurityUtils.getAccountId());
        Page<DutyPublicFreeDto> page = dutyPublicFreeService.findPage(pageable,dutyPublicFreeQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public DutyPublicFreeQuery getQuery(DutyPublicFreeQuery dutyPublicFreeQuery) {
        dutyPublicFreeQuery.setDateList( DutyDateTypeEnum.values());
        return dutyPublicFreeQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DutyPublicFreeForm dutyPublicFreeForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyPublicFreeService.save(dutyPublicFreeForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dutyPublicFreeService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

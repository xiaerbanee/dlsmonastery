package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.DutyPublicFreeDto;
import net.myspring.basic.modules.hr.service.DutyPublicFreeService;
import net.myspring.basic.modules.hr.web.form.DutyPublicFreeForm;
import net.myspring.basic.modules.hr.web.query.DutyPublicFreeQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/dutyPublicFree")
public class DutyPublicFreeController {

    @Autowired
    private DutyPublicFreeService dutyPublicFreeService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:dutyPublicFree:view')")
    public Page<DutyPublicFreeDto> list(Pageable pageable, DutyPublicFreeQuery dutyPublicFreeQuery) {
        dutyPublicFreeQuery.setCreatedBy(RequestUtils.getAccountId());
        Page<DutyPublicFreeDto> page = dutyPublicFreeService.findPage(pageable,dutyPublicFreeQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public DutyPublicFreeQuery getQuery(DutyPublicFreeQuery dutyPublicFreeQuery) {
        dutyPublicFreeQuery.setDateList( DutyDateTypeEnum.getList());
        return dutyPublicFreeQuery;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'hr:dutyPublicFree:edit')")
    public RestResponse save(DutyPublicFreeForm dutyPublicFreeForm, BindingResult bindingResult) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        dutyPublicFreeService.save(dutyPublicFreeForm);
        return restResponse;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'hr:dutyPublicFree:delete')")
    public RestResponse delete(String id) {
        dutyPublicFreeService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

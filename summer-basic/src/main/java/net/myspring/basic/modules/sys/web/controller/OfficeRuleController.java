package net.myspring.basic.modules.sys.web.controller;

import net.myspring.common.enums.BoolEnum;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.service.OfficeRuleService;
import net.myspring.basic.modules.sys.web.form.OfficeRuleForm;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangzm on 2017/4/22.
 */
@RestController
@RequestMapping(value = "sys/officeRule")
public class OfficeRuleController {

    @Autowired
    private OfficeRuleService officeRuleService;
    
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'sys:officeRule:view')")
    public Page<OfficeRuleDto> list(Pageable pageable, OfficeRuleQuery officeRuleQuery) {
        Page<OfficeRuleDto> page = officeRuleService.findPage(pageable,officeRuleQuery);
        return page;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'sys:officeRule:edit')")
    public RestResponse save(OfficeRuleForm officeRuleForm) {
        officeRuleService.save(officeRuleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "findOne")
    public OfficeRuleDto findOne(OfficeRuleDto officeRuleDto){
        officeRuleDto=officeRuleService.findOne(officeRuleDto);
        return officeRuleDto;
    }

    @RequestMapping(value = "getForm")
    public OfficeRuleForm getForm(OfficeRuleForm officeRuleForm){
        officeRuleForm.getExtra().put("officeRuleList",officeRuleService.findAll());
        return officeRuleForm;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'sys:officeRule:delete')")
    public RestResponse delete(OfficeRuleForm officeRuleForm) {
        officeRuleService.logicDelete(officeRuleForm);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "getQuery")
    public OfficeRuleQuery getQuery(OfficeRuleQuery officeRuleQuery) {
        return officeRuleQuery;
    }
}

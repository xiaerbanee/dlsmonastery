package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.dto.BackendModuleDto;
import net.myspring.basic.modules.sys.service.BackendService;
import net.myspring.basic.modules.sys.web.form.BackendForm;
import net.myspring.basic.modules.sys.web.form.BackendModuleForm;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.sys.service.BackendModuleService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/backendModule")
public class BackendModuleController {

    @Autowired
    private BackendModuleService backendModuleService;
    @Autowired
    private BackendService backendService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'sys:backendModule:view')")
    public Page<BackendModuleDto> list(Pageable pageable, BackendModuleQuery backendModuleQuery){
        Page<BackendModuleDto> page = backendModuleService.findPage(pageable,backendModuleQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'sys:backendModule:delete')")
    public RestResponse delete(String id) {
        backendModuleService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'sys:backendModule:edit')")
    public RestResponse save(BackendModuleForm backendModuleForm) {
        backendModuleService.save(backendModuleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public BackendModuleDto findForm(BackendModuleDto backendModuleDto){
        backendModuleDto=backendModuleService.findOne(backendModuleDto);
        return backendModuleDto;
    }

    @RequestMapping(value = "getForm")
    public BackendModuleForm getForm(BackendModuleForm backendModuleForm){
        backendModuleForm.getExtra().put("backendList",backendService.findAll());
        return backendModuleForm;
    }

    @RequestMapping(value = "getQuery")
    public BackendModuleQuery getQuery(BackendModuleQuery backendModuleQuery){
        return backendModuleQuery;
    }
}

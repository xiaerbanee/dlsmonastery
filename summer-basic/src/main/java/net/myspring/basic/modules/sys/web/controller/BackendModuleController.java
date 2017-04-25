package net.myspring.basic.modules.sys.web.controller;

import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.dto.BackendModuleDto;
import net.myspring.basic.modules.sys.service.BackendService;
import net.myspring.basic.modules.sys.web.form.BackendForm;
import net.myspring.basic.modules.sys.web.form.BackendModuleForm;
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<BackendModuleDto> list(Pageable pageable, BackendModuleQuery backendModuleQuery){
        Page<BackendModuleDto> page = backendModuleService.findPage(pageable,backendModuleQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        backendModuleService.logicDeleteOne(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(BackendModuleForm backendModuleForm) {
        backendModuleService.save(backendModuleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public BackendModuleForm findForm(BackendModuleForm backendModuleForm){
        backendModuleForm=backendModuleService.findForm(backendModuleForm);
        backendModuleForm.setBackendList(backendService.findAll());
        return backendModuleForm;
    }

}

package net.myspring.general.modules.sys.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.general.modules.sys.domain.ProcessType;
import net.myspring.general.modules.sys.dto.ProcessTypeDto;
import net.myspring.general.modules.sys.service.ProcessTypeService;
import net.myspring.general.modules.sys.web.form.ProcessTypeForm;
import net.myspring.general.modules.sys.web.query.ProcessTypeQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "sys/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ProcessTypeDto> list(Pageable pageable, ProcessTypeQuery processTypeQuery){
        Page<ProcessTypeDto> page = processTypeService.findPage(pageable,processTypeQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        processTypeService.logicDeleteOne(id);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProcessTypeForm processTypeForm) {
        processTypeService.save(processTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }


}

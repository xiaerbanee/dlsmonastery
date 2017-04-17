package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessTypeDto;
import net.myspring.basic.modules.sys.service.ProcessTypeService;
import net.myspring.basic.modules.sys.web.form.ProcessTypeForm;
import net.myspring.basic.modules.sys.web.query.ProcessTypeQuery;
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
@RequestMapping(value = "sys/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private PositionService positionService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProcessTypeDto> list(Pageable pageable, ProcessTypeQuery processTypeQuery){
        Page<ProcessTypeDto> page = processTypeService.findPage(pageable,processTypeQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ProcessType processType,BindingResult bindingResult) {
        processTypeService.logicDeleteOne(processType);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProcessTypeForm processTypeForm) {
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ProcessTypeForm findOne(ProcessTypeForm processTypeForm){
        processTypeForm=processTypeService.findForm(processTypeForm);
        processTypeForm.setPositionList(positionService.findAll());
        processTypeForm.setBools(BoolEnum.getMap());
        return processTypeForm;
    }
}

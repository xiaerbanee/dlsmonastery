package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessTypeDto;
import net.myspring.basic.modules.sys.service.ProcessTypeService;
import net.myspring.basic.modules.sys.web.form.ProcessTypeForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<ProcessTypeDto> page = processTypeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(ProcessType processType,BindingResult bindingResult) {
        processTypeService.logicDeleteOne(processType);
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ProcessTypeForm processTypeForm) {
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功",ResponseCodeEnum.saved.name()));
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        ProcessTypeDto processTypeDto=processTypeService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(processTypeDto);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(String query){
        Map<String,Object> map= Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }
}

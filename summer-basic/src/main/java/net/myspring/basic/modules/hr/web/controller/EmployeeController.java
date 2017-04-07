package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "hr/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DictEnumService dictEnumService;

    @RequestMapping(value = "delete")
    public String delete(String id) {
        employeeService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(EmployeeForm employeeForm) {
        employeeService.save(employeeForm);
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        restResponse.getExtra().put("employeeId",employeeForm.getId());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        EmployeeDto employeeDto=employeeService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(employeeDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable,EmployeeQuery employeeQuery){
        Page<EmployeeDto> page = employeeService.findPage(pageable,employeeQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "search")
    public String search(String key){
        List<EmployeeDto> employeeList=employeeService.findByNameLike(key);
        return ObjectMapperUtils.writeValueAsString(employeeList);
    }


    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("statusList", EmployeeStatusEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "editForm",method = RequestMethod.GET)
    public String editForm(){
        Map<String,Object> map=Maps.newHashMap();
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }
}

package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountForm;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DictEnumService dictEnumService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        employeeService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(EmployeeForm employeeForm) {
        employeeService.save(employeeForm);
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        restResponse.getExtra().put("employeeId",employeeForm.getId());
        return restResponse;
    }

    @RequestMapping(value = "findDate")
    public Map<String,Object> findOne(String id){
        Map<String,Object> map=Maps.newHashMap();
        EmployeeDto employeeDto=employeeService.findDto(id);
        map.put("employee",employeeDto);
        AccountForm accountForm=new AccountForm();
        accountForm.setId(employeeDto.getAccountId());
        map.put("account",accountService.findForm(accountForm));
        return map;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<EmployeeDto> list(Pageable pageable,EmployeeQuery employeeQuery){
        Page<EmployeeDto> page = employeeService.findPage(pageable,employeeQuery);
        return page;
    }

    @RequestMapping(value = "search")
    public List<EmployeeDto> search(String key){
        List<EmployeeDto> employeeList=employeeService.findByNameLike(key);
        return employeeList;
    }


    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("positionDtoList",positionService.findAll());
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return map;
    }

    @RequestMapping(value="getQuery")
    public Map<String,Object> getQuery(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("statusList", EmployeeStatusEnum.values());
        return map;
    }

    @RequestMapping(value = "editForm",method = RequestMethod.GET)
    public Map<String,Object> editForm(){
        Map<String,Object> map=Maps.newHashMap();
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return map;
    }
}

package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.EmployeeService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
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
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        restResponse.getExtra().put("employeeId",employeeService.save(employeeForm).getId());
        return restResponse;
    }

    @RequestMapping(value = "findForm")
    public EmployeeForm findForm(EmployeeForm employeeForm){
        employeeForm=employeeService.findForm(employeeForm);
        employeeForm.setPositionList(positionService.findAll());
        employeeForm.setEducationList( dictEnumService.findValueByCategory(DictEnumCategoryEnum.EDUCATION.toString()));
        AccountForm accountForm=new AccountForm();
        accountForm.setId(employeeForm.getAccountId());
        employeeForm.setAccount(accountService.findForm(accountForm));
        return employeeForm;
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

    @RequestMapping(value = "findById")
    public List<EmployeeDto> findById(String id) {
        List<EmployeeDto> employeeDtoList =employeeService.findById(id);
        return employeeDtoList;
    }


    @RequestMapping(value="getQuery")
    public EmployeeQuery getQuery(EmployeeQuery employeeQuery){
        employeeQuery.setPositionList(positionService.findAll());
        employeeQuery.setStatusList(EmployeeStatusEnum.getList());
        return employeeQuery;
    }

    @RequestMapping(value = "editForm",method = RequestMethod.GET)
    public Map<String,Object> editForm(){
        Map<String,Object> map=Maps.newHashMap();
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.toString()));
        return map;
    }
}

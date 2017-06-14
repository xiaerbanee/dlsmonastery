package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.EmployeePhoneDto;
import net.myspring.future.modules.basic.service.EmployeePhoneService;
import net.myspring.future.modules.basic.web.form.EmployeePhoneForm;
import net.myspring.future.modules.basic.web.query.EmployeePhoneQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/employeePhone")
public class EmployeePhoneController {

    @Autowired
    private EmployeePhoneService employeePhoneService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<EmployeePhoneDto> list(Pageable pageable, EmployeePhoneQuery employeePhoneQuery){
        Page<EmployeePhoneDto> page = employeePhoneService.findPage(pageable,employeePhoneQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(EmployeePhoneForm employeePhoneForm) {
        employeePhoneService.logicDeleteOne(employeePhoneForm);
        return new RestResponse("删除成功",null);
    }

    @RequestMapping(value = "save")
    public RestResponse save(EmployeePhoneForm employeePhoneForm) {
        employeePhoneService.save(employeePhoneForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "findOne")
    public EmployeePhoneDto findOne(EmployeePhoneDto employeePhoneDto){
        employeePhoneDto=employeePhoneService.findOne(employeePhoneDto);
        return employeePhoneDto;
    }

    @RequestMapping(value="getForm")
    public EmployeePhoneForm getForm(EmployeePhoneForm employeePhoneForm){
        return employeePhoneForm;
    }

    @RequestMapping(value="getQuery")
    public EmployeePhoneDto getQuery(EmployeePhoneDto employeePhoneDto){
        return employeePhoneDto;
    }
}

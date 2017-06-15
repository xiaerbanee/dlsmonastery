package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.EmployeePhoneDepositDto;
import net.myspring.future.modules.basic.service.EmployeePhoneDepositService;
import net.myspring.future.modules.basic.web.form.EmployeePhoneDepositForm;
import net.myspring.future.modules.basic.web.query.EmployeePhoneDepositQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/2/17.
 */
@RestController
@RequestMapping(value = "hr/employeePhoneDeposit")
public class EmployeePhoneDepositController {

    @Autowired
    private EmployeePhoneDepositService employeePhoneDepositService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<EmployeePhoneDepositDto> list(Pageable pageable, EmployeePhoneDepositQuery employeePhoneDepositQuery){
        Page<EmployeePhoneDepositDto> page = employeePhoneDepositService.findPage(pageable,employeePhoneDepositQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(EmployeePhoneDepositForm employeePhoneDepositForm) {
        employeePhoneDepositService.logicDeleteOne(employeePhoneDepositForm);
        return new RestResponse("删除成功",null);
    }

    @RequestMapping(value = "save")
    public RestResponse save(EmployeePhoneDepositForm employeePhoneDepositForm) {
        employeePhoneDepositService.save(employeePhoneDepositForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(boolean pass,@RequestParam(value = "ids[]") String[] ids) {
        List<EmployeePhoneDepositDto> employeePhoneDepositList=employeePhoneDepositService.findByIds(Arrays.asList(ids));
        for(EmployeePhoneDepositDto employeePhoneDeposit:employeePhoneDepositList){
            if(StringUtils.isNotBlank(employeePhoneDeposit.getDepotOutId())){
                return new RestResponse("审核失败,"+employeePhoneDeposit.getDepotName()+"没有绑定财务门店；",null);
            }
        }
        employeePhoneDepositService.batchAudit(Arrays.asList(ids),pass);
        //同步金蝶
        return new RestResponse("批量审核成功",null);
    }

    @RequestMapping(value = "findOne")
    public EmployeePhoneDepositDto findOne(EmployeePhoneDepositDto employeePhoneDepositDto){
        return employeePhoneDepositDto;
    }

    @RequestMapping(value="getForm")
    public EmployeePhoneDepositForm getForm(EmployeePhoneDepositForm employeePhoneDepositForm){
        return employeePhoneDepositForm;
    }

    @RequestMapping(value="getQuery")
    public EmployeePhoneDepositQuery getListProperty(EmployeePhoneDepositQuery employeePhoneDepositQuery){
        return employeePhoneDepositQuery;
    }
}
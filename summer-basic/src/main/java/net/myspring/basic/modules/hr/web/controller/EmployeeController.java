package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.EmployeeService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.EmployeeForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.DictEnumCategoryEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.text.StringUtils;
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
        employeeService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(EmployeeForm employeeForm) {
        RestResponse restResponse = new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        restResponse.getExtra().put("employeeId", employeeService.save(employeeForm).getId());
        return restResponse;
    }

    @RequestMapping(value = "findOne")
    public EmployeeDto findOne(EmployeeDto employeeDto) {
        employeeDto = employeeService.findOne(employeeDto);
        return employeeDto;
    }

    @RequestMapping(value = "getForm")
    public EmployeeForm findForm(EmployeeForm employeeForm) {
        employeeForm.getExtra().put("positionList", positionService.findAll());
        employeeForm.getExtra().put("educationList", dictEnumService.findValueByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return employeeForm;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<EmployeeDto> list(Pageable pageable, EmployeeQuery employeeQuery) {
        Page<EmployeeDto> page = employeeService.findPage(pageable, employeeQuery);
        return page;
    }

    @RequestMapping(value = "search")
    public List<EmployeeDto> search(String key) {
        List<EmployeeDto> employeeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(key)) {
            employeeList = employeeService.findByNameLike(key);
        }
        return employeeList;
    }

    @RequestMapping(value = "findById")
    public EmployeeDto findOne(String id) {
        return employeeService.findOne(id);
    }


    @RequestMapping(value = "getQuery")
    public EmployeeQuery getQuery(EmployeeQuery employeeQuery) {
        employeeQuery.getExtra().put("positionList", positionService.findAll());
        employeeQuery.getExtra().put("statusList", EmployeeStatusEnum.getList());
        return employeeQuery;
    }

    @RequestMapping(value = "editForm", method = RequestMethod.GET)
    public Map<String, Object> editForm() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return map;
    }

    @RequestMapping(value = "findByIds")
    public List<EmployeeDto> findByIds(String idStr) {
        List<String> ids = StringUtils.getSplitList(idStr, CharConstant.COMMA);
        List<EmployeeDto> employeeDtoList = employeeService.findByIds(ids);
        return employeeDtoList;
    }

    @RequestMapping(value = "findAll")
    public List<EmployeeDto> findAll() {
        List<EmployeeDto> employeeDtoList = employeeService.findAll();
        return employeeDtoList;
    }

}

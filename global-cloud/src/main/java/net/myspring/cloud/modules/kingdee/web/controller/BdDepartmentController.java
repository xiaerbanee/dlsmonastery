package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.service.BdDepartmentService;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "kingdee/bdDepartment")
public class BdDepartmentController {
    @Autowired
    private BdDepartmentService bdDepartmentService;

    @RequestMapping(value = "getByCustomerId", method = RequestMethod.GET)
    public BdDepartment getByCustomerId(String customerId) {
        BdDepartment department = bdDepartmentService.findByCustomerId(customerId);
        return department;
    }

    @RequestMapping(value = "getDepartmentList", method = RequestMethod.GET)
    public List<BdDepartment> getDepartmentList() {
        List<BdDepartment> departmentList = bdDepartmentService.findAll();
        return departmentList;
    }

}

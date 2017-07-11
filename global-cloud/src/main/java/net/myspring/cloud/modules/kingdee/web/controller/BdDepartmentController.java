package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.service.BdDepartmentService;
import net.myspring.cloud.modules.kingdee.web.query.BdDepartmentQuery;
import net.myspring.cloud.modules.kingdee.web.query.BdSupplierQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/5/17.
 */
@RestController
@RequestMapping(value = "kingdee/bdDepartment")
public class BdDepartmentController {
    @Autowired
    private BdDepartmentService bdDepartmentService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BdDepartment> list(Pageable pageable, BdDepartmentQuery bdDepartmentQuery){
        Page<BdDepartment> page = bdDepartmentService.findPageIncludeForbid(pageable,bdDepartmentQuery);
        return page;
    }

    @RequestMapping(value = "findByNameLike")
    public List<BdDepartment> findByNameLike(String name){
        List<BdDepartment> bdDepartmentList = bdDepartmentService.findByNameLike(name);
        return bdDepartmentList;
    }

    @RequestMapping(value = "findAll")
    public List<BdDepartment> findAll(){
        return bdDepartmentService.findAll();
    }

    @RequestMapping(value = "findCustId")
    public BdDepartment findCustId(String custId){
        return bdDepartmentService.findByCustId(custId);
    }

    //应付报表
    @RequestMapping(value = "getQueryForSupplierPayable")
    public BdDepartmentQuery getQueryForSupplierPayable(){
        return bdDepartmentService.getQueryForSupplierPayable();
    }
}

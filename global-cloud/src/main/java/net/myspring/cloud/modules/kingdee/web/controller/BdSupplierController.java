package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdSupplier;
import net.myspring.cloud.modules.kingdee.service.BdSupplierService;
import net.myspring.cloud.modules.kingdee.web.query.BdSupplierQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供应商
 * Created by lihx on 2017/6/13.
 */
@RestController
@RequestMapping(value = "kingdee/bdSupplier")
public class BdSupplierController {
    @Autowired
    private BdSupplierService bdSupplierService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<BdSupplier> list(Pageable pageable, BdSupplierQuery bdSupplierQuery){
        Page<BdSupplier> page = bdSupplierService.findPageIncludeForbid(pageable,bdSupplierQuery);
        return page;
    }

    @RequestMapping(value = "findByNameLike")
    public List<BdSupplier> findByNameLike(String name){
        return bdSupplierService.findByNameLike(name);
    }

    //应付报表
    @RequestMapping(value = "getQueryForSupplierPayable")
    public BdSupplierQuery getQueryForSupplierPayable(){
        return bdSupplierService.getQueryForSupplierPayable();
    }
}

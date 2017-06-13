package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdSupplier;
import net.myspring.cloud.modules.kingdee.service.BdSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "findByNameLike")
    public List<BdSupplier> findByNameLike(String name){
        return bdSupplierService.findByNameLike(name);
    }
}

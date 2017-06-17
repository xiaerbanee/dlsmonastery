package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.cloud.modules.kingdee.service.BdStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdStock")
public class BdStockController {
    @Autowired
    private BdStockService bdStockService;

    @RequestMapping(value = "findByNameLike")
    public List<BdStock> findByNameLike(String name){
        return bdStockService.findByNameLike(name);
    }

    @RequestMapping(value = "findAll")
    public List<BdStock> findAll(){
        return bdStockService.findAll();
    }
}

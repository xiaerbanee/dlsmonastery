package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.kingdee.service.StkInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 库存
 * Created by lihx on 2017/6/16.
 */
@RestController
@RequestMapping(value = "kingdee/stkInventory")
public class StkInventoryController {
    @Autowired
    private StkInventoryService stkInventoryService;

    @RequestMapping(value = "findByStockIds",method = RequestMethod.POST)
    public List<StkInventory> findByStockIds(@RequestBody List<String> depotStoreOutIds){
        return stkInventoryService.findByStockIds(depotStoreOutIds);
    }

    @RequestMapping(value = "findByMaterialIds",method = RequestMethod.POST)
    public List<StkInventory> findByMaterialIdList(@RequestBody List<String> productOutIds){
        return stkInventoryService.findByMaterialIdList(productOutIds);
    }
}

package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.kingdee.service.StkInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "findByStockIds")
    public List<StkInventory> findByStockIds(List<String> stockIds){
        return stkInventoryService.findByStockIds(stockIds);
    }

    @RequestMapping(value = "findByMaterialIds",method = RequestMethod.GET)
    public List<StkInventory> findByMaterialIdList(List<String> materialIdList){
        return stkInventoryService.findByMaterialIdList(materialIdList);
    }
}

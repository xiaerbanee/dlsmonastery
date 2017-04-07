package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.kingdee.service.StkInventoryService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "kingdee/stkInventory")
public class StkInventoryController {
    @Autowired
    private StkInventoryService stkInventoryService;

    //库存
    @RequestMapping(value = "getInventoryList", method = RequestMethod.GET)
    public List<StkInventory>  getInventoryList(@RequestParam(value = "stockIds")List<String> stockIds) {
        List<StkInventory>  bdInventoryList = stkInventoryService.findByfStockIds(stockIds);
        return bdInventoryList;
    }

    @RequestMapping(value = "getByDepotAndProduct", method = RequestMethod.GET)
    public  StkInventory findBdInventoryByDepotAndProduct(String companyName,String depotId,String productId){
        StkInventory inventory = stkInventoryService.findByStockIdAndMaterialId(depotId,productId);
        return inventory;
    }
}

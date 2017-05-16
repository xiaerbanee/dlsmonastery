package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.service.GoodsOrderDetailService;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "crm/goodsOrderDetail")
public class GoodsOrderDetailController {

    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    @ModelAttribute
    public GoodsOrderDetail get(@RequestParam(required = false) String id) {
        return null;
    }


    @RequestMapping(value = "getFormListForNewWithoutAreaQty")
    public List<GoodsOrderDetailForm> getFormListForNewWithoutAreaQty(String depotId, String netType, String shipType) {

        return goodsOrderDetailService.getFormListForNewWithoutAreaQty( depotId, netType, shipType);

    }

}

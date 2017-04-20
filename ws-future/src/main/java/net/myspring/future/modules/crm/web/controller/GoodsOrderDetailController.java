package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.service.GoodsOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "crm/goodsOrderDetail")
public class GoodsOrderDetailController {

    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    @ModelAttribute
    public GoodsOrderDetail get(@RequestParam(required = false) String id) {
        return null;
    }

}

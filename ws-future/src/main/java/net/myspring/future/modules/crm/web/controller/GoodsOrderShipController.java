package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.service.GoodsOrderShipService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/goodsOrderShip")
public class GoodsOrderShipController {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private GoodsOrderShipService goodsOrderShipService;



    @RequestMapping(method = RequestMethod.GET)
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        Page<GoodsOrderDto> page = goodsOrderShipService.findAll(pageable, goodsOrderQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.setNetTypeList(NetTypeEnum.getList());
        goodsOrderQuery.setShipTypeList(ShipTypeEnum.getList());
        goodsOrderQuery.setStatusList(Arrays.asList("待发货","待签收","已完成"));
        return goodsOrderQuery;
    }

    @RequestMapping(value = "findOne")
    public GoodsOrderDto findOne(String id) {
        return goodsOrderService.findOne(id);
    }
}

package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderPrintDto;
import net.myspring.future.modules.crm.service.GoodsOrderShipService;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/goodsOrderShip")
public class GoodsOrderShipController {

    @Autowired
    private GoodsOrderShipService goodsOrderShipService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:view')")
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        return goodsOrderShipService.findAll(pageable, goodsOrderQuery);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:view')")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.getExtra().put("netTypeList",NetTypeEnum.getList());
        goodsOrderQuery.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        goodsOrderQuery.getExtra().put("statusList",Arrays.asList("待发货","待签收","已完成"));
        return goodsOrderQuery;
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:edit')")
    public GoodsOrderShipForm getForm(GoodsOrderShipForm goodsOrderShipForm) {
        return goodsOrderShipForm;
    }

    @RequestMapping(value = "shipCheck")
    public  Map<String,Object> shipCheck(GoodsOrderShipForm goodsOrderShipForm) {
        return goodsOrderShipService.shipCheck(goodsOrderShipForm);
    }

    @RequestMapping(value = "ship")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:ship')")
    public RestResponse ship(GoodsOrderShipForm goodsOrderShipForm) {
         goodsOrderShipService.ship(goodsOrderShipForm);
         return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "sign")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:sign')")
    public RestResponse sign(String goodsOrderId) {
        goodsOrderShipService.sign(goodsOrderId);
        return new RestResponse("签收成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "shipBack")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:shipBack')")
    public RestResponse shipBack(String goodsOrderId) {
        goodsOrderShipService.shipBack(goodsOrderId);
        return new RestResponse("重发成功",ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "sreturn")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:sreturn')")
    public RestResponse sreturn(GoodsOrderForm goodsOrderForm) {
        goodsOrderShipService.sreturn(goodsOrderForm);
        return new RestResponse("销售退货成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getSreturn")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:sreturn')")
    public GoodsOrderDto getSreturn(String id) {
        return goodsOrderShipService.getSreturn(id);
    }

    @RequestMapping(value = "getShip")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:ship')")
    public GoodsOrderDto getShip(String id) {
        return goodsOrderShipService.getShip(id);
    }

    @RequestMapping(value = "getShipByBusinessId")
    @PreAuthorize("hasPermission(null,'crm:goodsOrderShip:ship')")
    public GoodsOrderDto getShipByBusinessId(String businessId) {
        if(StringUtils.isBlank(businessId)){
            return new GoodsOrderDto();
        }
        return goodsOrderShipService.getShipByBusinessId(businessId.replaceAll(FormatterConstant.GOODS_ORDER, ""));
    }

    @RequestMapping(value = "print")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:print')")
    public GoodsOrderPrintDto print(String goodsOrderId) {
        return goodsOrderShipService.print(goodsOrderId);
    }

    @RequestMapping(value = "shipPrint")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:print')")
    public GoodsOrderPrintDto shipPrint(String goodsOrderId) {
        return goodsOrderShipService.shipPrint(goodsOrderId);
    }
}

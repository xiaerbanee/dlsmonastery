package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        Page<GoodsOrderDto> page = goodsOrderService.findAll(pageable, goodsOrderQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.setNetTypeList(NetTypeEnum.getList());
        goodsOrderQuery.setShipTypeList(ShipTypeEnum.getList());
        goodsOrderQuery.setStatusList(GoodsOrderStatusEnum.getList());
        return goodsOrderQuery;
    }


    @RequestMapping(value = "getForm")
    public GoodsOrderForm getForm(GoodsOrderForm goodsOrderForm){
        goodsOrderForm.setNetTypeList(NetTypeEnum.getList());
        goodsOrderForm.setShipTypeList(ShipTypeEnum.getList());
        return goodsOrderForm;
    }

    @RequestMapping(value = "findGoodsOrderDetailFormList")
    public List<GoodsOrderDetailForm> findGoodsOrderDetailFormList(String id,String shopId,String netType,String shipType) {
        return goodsOrderService.findGoodsOrderDetailFormList(id,shopId,netType,shipType);
    }

    @RequestMapping(value = "save")
    public RestResponse save(GoodsOrderForm goodsOrderForm) {
        goodsOrderService.save(goodsOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBillForm")
    public GoodsOrderBillForm getBillForm(String id){
        GoodsOrderBillForm goodsOrderBillForm = goodsOrderService.getGoodsOrderBillForm(id);
        //设置仓库
        DepotQuery depotQuery = new DepotQuery();
        depotQuery.setShipType(goodsOrderBillForm.getShipType());
        goodsOrderBillForm.setStoreList(depotService.findStoreList(depotQuery));
        goodsOrderBillForm.setExpressCompanyDtoList(expressCompanyService.findAll());
        return goodsOrderBillForm;
    }



    @RequestMapping(value = "findOne")
    public GoodsOrderDto findOne(String id) {
        return goodsOrderService.findOne(id);
    }
}

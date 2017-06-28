package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "detail")
    public GoodsOrderDto detail(String id) {
        return goodsOrderService.findDetail(id);
    }

    @RequestMapping(value = "getQuery")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.getExtra().put("netTypeList",NetTypeEnum.getList());
        goodsOrderQuery.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        goodsOrderQuery.getExtra().put("statusList",GoodsOrderStatusEnum.getList());
        return goodsOrderQuery;
    }


    @RequestMapping(value = "getForm")
    public GoodsOrderForm getForm(GoodsOrderForm goodsOrderForm){
        goodsOrderForm.getExtra().put("netTypeList",NetTypeEnum.getList());
        goodsOrderForm.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        return goodsOrderForm;
    }

    @RequestMapping(value = "findDetailList")
    public List<GoodsOrderDetailDto> findDetailList(String id, String shopId, String netType, String shipType) {
        return goodsOrderService.findDetailList(id,shopId,netType,shipType);
    }

    @RequestMapping(value = "save")
    public RestResponse save(GoodsOrderForm goodsOrderForm) {
        goodsOrderService.save(goodsOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBillForm")
    public GoodsOrderBillForm getBillForm(GoodsOrderBillForm goodsOrderBillForm){
        //设置仓库
        GoodsOrderDto goodsOrderDto = goodsOrderService.findOne(goodsOrderBillForm.getId());
        DepotQuery depotQuery = new DepotQuery();
        depotQuery.setShipType(goodsOrderDto.getShipType());
        goodsOrderBillForm.getExtra().put("storeList",depotService.findStoreList(depotQuery));
        goodsOrderBillForm.getExtra().put("expressCompanyList",expressCompanyService.findAll());
        return goodsOrderBillForm;
    }

    @RequestMapping(value = "getBill")
    public GoodsOrderDto getBill(String id) {
        return goodsOrderService.getBill(id);
    }

    @RequestMapping(value = "bill")
    public RestResponse bill(GoodsOrderBillForm goodsOrderBillForm) {
        if(CollectionUtil.isEmpty(goodsOrderBillForm.getGoodsOrderBillDetailFormList())){
            throw new ServiceException("开单明细不能为空");
        }
        for(GoodsOrderBillDetailForm goodsOrderBillDetailForm : goodsOrderBillForm.getGoodsOrderBillDetailFormList()){
            if(goodsOrderBillDetailForm.getPrice()==null || goodsOrderBillDetailForm.getPrice().compareTo(BigDecimal.ZERO)<0){
                throw new ServiceException("开单明细的单价必须大于等于0");
            }
        }
        goodsOrderService.bill(goodsOrderBillForm);
        return new RestResponse("开单成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public GoodsOrderDto findOne(String id) {
        return goodsOrderService.findOne(id);
    }

    @RequestMapping(value="delete")
    public RestResponse delete(String id) {
        goodsOrderService.delete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }
}

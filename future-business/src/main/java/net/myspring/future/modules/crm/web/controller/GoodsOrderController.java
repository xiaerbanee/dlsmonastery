package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.DepotUtils;
import net.myspring.future.common.service.DomainUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.service.*;
import net.myspring.future.modules.crm.validator.GoodsOrderValidator;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.domain.CompanyConfig;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ExpressOrderService expressOrderService;
    @Autowired
    private GoodsOrderValidator goodsOrderValidator;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private ShopGoodsDepositService shopGoodsDepositService;
    @Autowired
    private K3cloudSynService k3cloudSynService;

    @ModelAttribute
    public GoodsOrder get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new GoodsOrder() : goodsOrderService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Object businessId = searchEntity.getParams().get("businessId");
        if (businessId != null) {
            searchEntity.getParams().put("businessId", ((String) businessId).replace("XK", ""));
        }
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<GoodsOrder> page = goodsOrderService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for (GoodsOrder goodsOrder : page.getContent()) {
            goodsOrder.setActionList(getActionList(goodsOrder));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme(GoodsOrder goodsOrder, BindingResult bindingResult) {
        goodsOrder.setCurrentAction("shipBoxAndIme");
        goodsOrderValidator.validate(goodsOrder,bindingResult);
        if(bindingResult.hasErrors()){
            return ObjectMapperUtils.writeValueAsString(new RestResponse(false, bindingResult, MessageTypeEnum.error.name()));
        }
        goodsOrderService.shipBoxAndIme(goodsOrder);
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty(GoodsOrder goodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("shipTypes", GoodsOrderShipTypeEnum.values());
        String companyName = AccountUtils.getAccount().getCompanyName();
        if("JXOPPO".equalsIgnoreCase(companyName)||"JXVIVO".equalsIgnoreCase(companyName)){
            map.put("netTypes",Lists.newArrayList(NetTypeEnum.移动.name(),NetTypeEnum.联信.name()));
        }else {
            map.put("netTypes",Lists.newArrayList(NetTypeEnum.全网通.name()));
        }
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getGoodsOrderDetail", method = RequestMethod.GET)
    public String getGoodsOrderDetail(GoodsOrder goodsOrder, BindingResult bindingResult) {
        goodsOrder.setCurrentAction("form");
        if(StringUtils.isNotBlank(goodsOrder.getShopId())&&StringUtils.isNotBlank(goodsOrder.getNetType())){
            goodsOrderValidator.validate(goodsOrder,bindingResult);
            if(bindingResult.hasErrors()){
                return ObjectMapperUtils.writeValueAsString(new RestResponse(false,bindingResult,MessageTypeEnum.error.name()));
            }
            goodsOrderService.findGoodsOrderDetail(goodsOrder);
        }
        return ObjectMapperUtils.writeValueAsString(goodsOrder.getGoodsOrderDetailList());
    }


    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("status", GoodsOrderStatusEnum.values());
        map.put("expressCompanys", expressCompanyService.findAll());
        map.put("areas", officeService.findByType("100"));
        map.put("shipTypes", Arrays.asList(GoodsOrderShipTypeEnum.values()));
        map.put("netTypes",NetTypeEnum.values());
        map.put("stores", depotService.findStores());
        map.put("storeTypes", DepotUtils.getTypeMapByCategory(DepotCategoryEnum.STORE.name()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findByFormatId", method = RequestMethod.GET)
    public Object findByFormatId(String formatId) {
        GoodsOrder goodsOrder = goodsOrderService.findByFormatId(formatId);
        if (goodsOrder == null) {
            return new RestResponse(false, "该单号不存在", MessageTypeEnum.error.name());
        }
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "detail")
    public String detail(GoodsOrder goodsOrder) {
        Map<String,Object> map=Maps.newHashMap();
        map.putAll(goodsOrderService.findBtnCopy(goodsOrder));
        map.put("goodsOrder",goodsOrder);
        DomainUtils.initAuditing(goodsOrder.getGoodsOrderImeList());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "ship", method = RequestMethod.GET)
    public String ship(GoodsOrder goodsOrder) {
        depotService.initDomain(Lists.newArrayList(goodsOrder.getShop()));
        for (GoodsOrderDetail goodsOrderDetail : goodsOrder.getGoodsOrderDetailList()) {
            goodsOrderDetail.getExtendMap().put("waitShipQty", goodsOrderDetail.getRealBillQty() - goodsOrderDetail.getShippedQty());
        }
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "ship", method = RequestMethod.POST)
    public RestResponse shipSave(GoodsOrder goodsOrder, String expressCodes, BindingResult bindingResult) {
        goodsOrder.setCurrentAction("ship");
        goodsOrderValidator.validate(goodsOrder, bindingResult);
        if (bindingResult.hasErrors()) {
            return new RestResponse(false, bindingResult, MessageTypeEnum.error.name());
        }
        goodsOrderService.ship(goodsOrder);
        ExpressOrder expressOrder = expressOrderService.findOne(goodsOrder.getExpressOrderId());
        expressOrderService.save(ExpressOrderTypeEnum.手机订单.name(), goodsOrder.getId(), expressCodes, expressOrder.getExpressCompanyId());
        return new RestResponse("订单发货成功");
    }

    @RequestMapping(value = "print")
    public String print(GoodsOrder goodsOrder) {
        goodsOrderService.print(goodsOrder);
        if(StringUtils.isEmpty(goodsOrder.getOutCode())){

        }
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "shipPrint")
    public String shipPrint(GoodsOrder goodsOrder) {
        goodsOrderService.shipPrint(goodsOrder);
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "sign", method = RequestMethod.GET)
    public String signForm(GoodsOrder goodsOrder) {
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public RestResponse sign(GoodsOrder goodsOrde) {
        goodsOrderService.sign(goodsOrde);
        return new RestResponse("签收成功");
    }

    @RequestMapping(value = "shipBack", method = RequestMethod.GET)
    public RestResponse reship(GoodsOrder goodsOrder, RedirectAttributes redirectAttributes) {
        List<String> officeIds= FilterUtils.getOfficeIds(AccountUtils.getAccountId());
        if(Collections3.isNotEmpty(officeIds)&&officeIds.contains(goodsOrder.getStore().getOfficeId())){
            goodsOrderService.shipBack(goodsOrder);
        }
        return new RestResponse("订单重新发货成功，请重新扫描串码");
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(GoodsOrder goodsOrder, RedirectAttributes redirectAttributes) {
        if (StringUtils.isNotBlank(goodsOrder.getOutCode())) {

        } else {
            goodsOrderService.delete(goodsOrder);
        }
        return new RestResponse("删除成功");
    }


    @RequestMapping(value = "sreturnFormProperty", method = RequestMethod.GET)
    public String sreturn() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "sreturn", method = RequestMethod.GET)
    public String sreturnForm(GoodsOrder goodsOrder) {
        depotService.initDomain(Lists.newArrayList(goodsOrder.getShop()));
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "sreturn", method = RequestMethod.POST)
    public RestResponse sreturn(GoodsOrder goodsOrder) {
        if (!GoodsOrderStatusEnum.待发货.toString().equals(goodsOrder.getStatus()) || StringUtils.isBlank(goodsOrder.getOutCode())) {
            return new RestResponse(false, "订单不是待发货状态，无法销售退货", MessageTypeEnum.error.name());
        }
        if (StringUtils.isBlank(AccountUtils.getAccount().getOutId())) {
            return new RestResponse(false, "您没有财务关联账户，无法销售退货", MessageTypeEnum.error.name());
        }
        goodsOrderService.sreturn(goodsOrder);
        if(goodsOrder.getSyn()){
            k3cloudSynService.syn(goodsOrder.getId(), CloudSynExtendTypeEnum.货品订货.name(),GoodsOrder.class);
        }
        return new RestResponse("订单销售退货成功");
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(GoodsOrder goodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stores", goodsOrderService.findStores(goodsOrder));
        map.put("expressCompanys", expressCompanyService.findByExpressType(ExpressTypeEnum.手机订单.name()));
        map.put("bools", BoolEnum.getMap());
        map.put("proxys",DepotUtils.getTypeValueByCategory(DepotCategoryEnum.PROXY_SHOP.name()));
        CompanyConfig goodsOrderRebateRule = companyConfigService.findByCode(CompanyConfigCodeEnum.GOODS_ORDER_REBATE_RULE.name());
        if (goodsOrderRebateRule != null) {
            map.put("goodsOrderRebateRule", goodsOrderRebateRule);
        }
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "billChange", method = RequestMethod.GET)
    public String billChange(GoodsOrder goodsOrder) {
        depotService.initDomain(Lists.newArrayList(goodsOrder.getShop()));
        goodsOrderService.getBillChange(goodsOrder);
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "bill", method = RequestMethod.GET)
    public String bill(GoodsOrder goodsOrder) {
        goodsOrderService.getBillGoodsOrder(goodsOrder);
        depotService.initDomain(Lists.newArrayList(goodsOrder.getShop()));
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }

    @RequestMapping(value = "bill", method = RequestMethod.POST)
    public RestResponse billSave(GoodsOrder goodsOrder, BindingResult bindingResult) {
        try {
            goodsOrder.setCurrentAction("bill");
            goodsOrderValidator.validate(goodsOrder,bindingResult);
            if(bindingResult.hasErrors()){
                return new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
            }
            if (!GoodsOrderStatusEnum.待开单.toString().equals(goodsOrder.getStatus())) {
                return new RestResponse(false, "订单不是待开单状态，无法开单", MessageTypeEnum.error.name());
            }
            // 检查用户
            if(StringUtils.isBlank(goodsOrder.getShop().getRealOutId())){
                goodsOrder.setSyn(false);
            }
            //门店定金
            BigDecimal totalAmount = shopGoodsDepositService.getTotalAmount(goodsOrder.getShopId());
            if (totalAmount != null) {
                goodsOrder.getShop().setGoodsDeposit(totalAmount);
            }
            goodsOrderService.bill(goodsOrder);
            if(goodsOrder.getSyn()){
                k3cloudSynService.syn(goodsOrder.getId(), CloudSynExtendTypeEnum.货品订货.name(),GoodsOrder.class);
                if(goodsOrder.getExpressOrderId()!=null){
                    ExpressOrder expressOrder = expressOrderService.findOne(goodsOrder.getExpressOrderId());
                    expressOrder.setOutCode(k3cloudSynService.getOutCode(goodsOrder.getId(), CloudSynExtendTypeEnum.货品订货.name()));
                    expressOrderService.update(expressOrder);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new RestResponse(false,e.getMessage(), MessageTypeEnum.error.name());
        }
        return new RestResponse( "开单成功");
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(GoodsOrder goodsOrder) {
        if (!goodsOrder.isCreate() && !GoodsOrderStatusEnum.待开单.name().equals(goodsOrder.getStatus())) {
            return new RestResponse(false, "订单已开单，无法修改", MessageTypeEnum.error.name());
        } else {
            goodsOrderService.save(goodsOrder);
        }
        return new RestResponse("订单保存成功");
    }

    @RequestMapping(value = "findOne")
    public String findOne(GoodsOrder goodsOrder) {
        return ObjectMapperUtils.writeValueAsString(goodsOrder);
    }


    private List<String> getActionList(GoodsOrder goodsOrder) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:goodsOrder:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        if(GoodsOrderStatusEnum.待开单.name().equals(goodsOrder.getStatus())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:goodsOrder:bill" : actionList.add(Const.ITEM_ACTION_BILL); break;
                    case "crm:goodsOrder:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:goodsOrder:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
        }else if(GoodsOrderStatusEnum.待发货.name().equals(goodsOrder.getStatus())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:goodsOrder:ship" : actionList.add(Const.ITEM_ACTION_SHIP); break;
                    case "crm:goodsOrder:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:goodsOrder:bill" : actionList.add(Const.ITEM_ACTION_SRETURN); break;
                    case "crm:goodsOrder:print" : actionList.add(Const.ITEM_ACTION_SHIP_PRINT);actionList.add(Const.ITEM_ACTION_PRINT); break;
                    default : break;
                }
            }
        }else if(GoodsOrderStatusEnum.待签收.name().equals(goodsOrder.getStatus())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:goodsOrder:edit" : actionList.add(Const.ITEM_ACTION_SIGN_IN); break;
                    case "crm:goodsOrder:shipBack" : actionList.add(Const.ITEM_ACTION_REPEAT); break;
                    default : break;
                }
            }
        }else {
            actionList.add(Const.ITEM_ACTION_SHIP_BACK);
        }
        return actionList;
    }
}

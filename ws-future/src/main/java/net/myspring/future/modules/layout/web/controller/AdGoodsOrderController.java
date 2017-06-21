package net.myspring.future.modules.layout.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailSimpleDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.service.AdGoodsOrderService;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderAuditForm;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderBillForm;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderForm;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderShipForm;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "layout/adGoodsOrder")
public class  AdGoodsOrderController {

    @Autowired
    private AdGoodsOrderService adGoodsOrderService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AdGoodsOrderDto> list(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
        return adGoodsOrderService.findPage(pageable,adGoodsOrderQuery);
    }

    @RequestMapping(value = "getQuery")
    public AdGoodsOrderQuery getQuery(AdGoodsOrderQuery adGoodsOrderQuery) {
        adGoodsOrderQuery.getExtra().put("billTypeList",BillTypeEnum.getList());
        adGoodsOrderQuery.getExtra().put("areaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return adGoodsOrderQuery;
    }

    @RequestMapping(value = "audit")
    public RestResponse audit(AdGoodsOrderAuditForm adGoodsOrderAuditForm) {
        adGoodsOrderService.audit(adGoodsOrderAuditForm);
        return new RestResponse("审核成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "outShopChange", method = RequestMethod.GET)
    public String outShopChange(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("adGoodsOrder", adGoodsOrder);
        return null;
    }

    @RequestMapping(value = "findDto")
    public AdGoodsOrderDto findDto(String id){
        return adGoodsOrderService.findDto(id);
    }


    @RequestMapping(value = "getForm", method = RequestMethod.GET)
    public AdGoodsOrderForm getForm(AdGoodsOrderForm adGoodsOrderForm) {
//        adGoodsOrderForm.setE
//        adGoodsOrderForm = adGoodsOrderService.getForm(adGoodsOrderForm);
        adGoodsOrderForm.setExpressOrderExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
        return adGoodsOrderForm;
    }

    @RequestMapping(value = "getShipForm")
    public AdGoodsOrderShipForm getShipForm(AdGoodsOrderShipForm adGoodsOrderShipForm) {
        return adGoodsOrderShipForm;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(AdGoodsOrderForm adGoodsOrderForm) {
        if(CollectionUtil.isEmpty(adGoodsOrderForm.getAdGoodsOrderDetailList())){
            throw new ServiceException("请录入柜台订货明细");
        }

        adGoodsOrderService.save(adGoodsOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("type", "标准出库单");
        return null;
    }

    @RequestMapping(value = "getBillForm")
    public AdGoodsOrderBillForm getBillForm(AdGoodsOrderBillForm adGoodsOrderBillForm) {

        adGoodsOrderBillForm.getExtra().put("adStoreList",depotService.findAdStoreDtoList() );

        String defaultStoreId=CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue();
        adGoodsOrderBillForm.getExtra().put("defaultStoreId",defaultStoreId);

        adGoodsOrderBillForm.getExtra().put("defaultBillDate", LocalDate.now());
        adGoodsOrderBillForm.getExtra().put("defaultExpressCompanyId", expressCompanyService.getDefaultExpressCompanyId());

        return adGoodsOrderBillForm;
    }

    @RequestMapping(value = "bill")
    public RestResponse bill(AdGoodsOrderBillForm adGoodsOrderBillForm) {

        //TODO 开单前校验

//        // 检查用户
//        if (adGoodsOrderBillForm.getSyn()!=null && adGoodsOrderBillForm.getSyn()) {
//            RequestUtils.getRequestEntity().get
//            AccountCommonDto accN
//            if (StringUtils.isBlank(AccountUtils.getAccount().getOutId())) {
//                return new Message("message_ad_goods_order_not_finance", Message.Type.danger);
//            }
//        }

        adGoodsOrderService.bill(adGoodsOrderBillForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "getYsyfMap")
    public Map<String, Object> getYsyfMap(String adGoodsOrderId) {
        if(StringUtils.isBlank(adGoodsOrderId)){
            return null;
        }
        return adGoodsOrderService.getYsyfMap(adGoodsOrderId);

    }

    @RequestMapping(value = "ship")
    public RestResponse ship(AdGoodsOrderShipForm adGoodsOrderShipForm) {
        adGoodsOrderService.ship(adGoodsOrderShipForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "sign")
    public RestResponse sign(String id) {
        adGoodsOrderService.sign(id);
        return new RestResponse("签收成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "print")
    public AdGoodsOrderDto print(String id) {
        return adGoodsOrderService.print(id);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderService.logicDelete(adGoodsOrderForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findDetailListForNewOrEdit")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListForNewOrEdit(String adGoodsOrderId, boolean includeNotAllowOrderProduct) {
        return adGoodsOrderService.findDetailListForNewOrEdit(adGoodsOrderId, includeNotAllowOrderProduct);
    }

    @RequestMapping(value = "findDetailListForBill")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListForBill(String adGoodsOrderId) {
        return adGoodsOrderService.findDetailListForBill(adGoodsOrderId);
    }

    @RequestMapping(value = "findDetailListByAdGoodsOrderId")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListByAdGoodsOrderId(String adGoodsOrderId) {
        return adGoodsOrderService.findDetailListByAdGoodsOrderId(adGoodsOrderId);
    }

    @RequestMapping(value="export")
    public String export(AdGoodsOrderQuery adGoodsOrderQuery) {
        return adGoodsOrderService.export(adGoodsOrderQuery);
    }



}

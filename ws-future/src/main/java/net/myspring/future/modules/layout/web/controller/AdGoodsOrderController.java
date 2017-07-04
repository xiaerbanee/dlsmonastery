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
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.elasticsearch.xpack.notification.email.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
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
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public Page<AdGoodsOrderDto> list(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
        return adGoodsOrderService.findPage(pageable,adGoodsOrderQuery);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public AdGoodsOrderQuery getQuery(AdGoodsOrderQuery adGoodsOrderQuery) {
        adGoodsOrderQuery.getExtra().put("billTypeList",BillTypeEnum.getList());
        adGoodsOrderQuery.getExtra().put("areaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return adGoodsOrderQuery;
    }

    @RequestMapping(value = "audit")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:edit')")
    public RestResponse audit(AdGoodsOrderAuditForm adGoodsOrderAuditForm) {
        adGoodsOrderService.audit(adGoodsOrderAuditForm);
        return new RestResponse("审核成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "outShopChange", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public String outShopChange(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("adGoodsOrder", adGoodsOrder);
        return null;
    }

    @RequestMapping(value = "findDto")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public AdGoodsOrderDto findDto(String id){
        return adGoodsOrderService.findDto(id);
    }


    @RequestMapping(value = "getForm", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public AdGoodsOrderForm getForm(AdGoodsOrderForm adGoodsOrderForm) {
//        adGoodsOrderForm.setE
//        adGoodsOrderForm = adGoodsOrderService.getForm(adGoodsOrderForm);
        adGoodsOrderForm.setExpressOrderExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
        return adGoodsOrderForm;
    }

    @RequestMapping(value = "getShipForm")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public AdGoodsOrderShipForm getShipForm(AdGoodsOrderShipForm adGoodsOrderShipForm) {
        return adGoodsOrderShipForm;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:edit')")
    public RestResponse save(AdGoodsOrderForm adGoodsOrderForm) {
        if(CollectionUtil.isEmpty(adGoodsOrderForm.getAdGoodsOrderDetailList())){
            throw new ServiceException("请录入柜台订货明细");
        }

        adGoodsOrderService.save(adGoodsOrderForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public String getBillFormProperty(AdGoodsOrder adGoodsOrder) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("type", "标准出库单");
        return null;
    }

    @RequestMapping(value = "getBillForm")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public AdGoodsOrderBillForm getBillForm(AdGoodsOrderBillForm adGoodsOrderBillForm) {

        adGoodsOrderBillForm.getExtra().put("adStoreList",depotService.findAdStoreDtoList() );

        String defaultStoreId=CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue();
        adGoodsOrderBillForm.getExtra().put("defaultStoreId",defaultStoreId);

        adGoodsOrderBillForm.getExtra().put("defaultBillDate", LocalDate.now());
        adGoodsOrderBillForm.getExtra().put("defaultExpressCompanyId", expressCompanyService.getDefaultExpressCompanyId());

        return adGoodsOrderBillForm;
    }

    @RequestMapping(value = "bill")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:bill')")
    public RestResponse bill(AdGoodsOrderBillForm adGoodsOrderBillForm) {
        adGoodsOrderService.bill(adGoodsOrderBillForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getYsyfMap")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public Map<String, Object> getYsyfMap(String adGoodsOrderId) {
        if(StringUtils.isBlank(adGoodsOrderId)){
            return null;
        }
        return adGoodsOrderService.getYsyfMap(adGoodsOrderId);

    }

    @RequestMapping(value = "ship")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:ship')")
    public RestResponse ship(AdGoodsOrderShipForm adGoodsOrderShipForm) {
        adGoodsOrderService.ship(adGoodsOrderShipForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "sign")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:sign')")
    public RestResponse sign(String id) {
        adGoodsOrderService.sign(id);
        return new RestResponse("签收成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "print")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:print')")
    public AdGoodsOrderDto print(String id) {
        return adGoodsOrderService.print(id);
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:delete')")
    public RestResponse delete(AdGoodsOrderForm adGoodsOrderForm) {
        adGoodsOrderService.logicDelete(adGoodsOrderForm.getId());
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findDetailListForNewOrEdit")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:edit')")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListForNewOrEdit(String adGoodsOrderId, boolean includeNotAllowOrderProduct) {
        return adGoodsOrderService.findDetailListForNewOrEdit(adGoodsOrderId, includeNotAllowOrderProduct);
    }

    @RequestMapping(value = "findDetailListForBill")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:bill')")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListForBill(String adGoodsOrderId) {
        return adGoodsOrderService.findDetailListForBill(adGoodsOrderId);
    }

    @RequestMapping(value = "findDetailListByAdGoodsOrderId")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public List<AdGoodsOrderDetailSimpleDto> findDetailListByAdGoodsOrderId(String adGoodsOrderId) {
        return adGoodsOrderService.findDetailListByAdGoodsOrderId(adGoodsOrderId);
    }

    @RequestMapping(value="export")
    @PreAuthorize("hasPermission(null,'crm:adGoodsOrder:view')")
    public ModelAndView export(AdGoodsOrderQuery adGoodsOrderQuery) throws IOException{
        return new ModelAndView(new ExcelView(),"simpleExcelBook",adGoodsOrderService.export(adGoodsOrderQuery));
    }



}

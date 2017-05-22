package net.myspring.future.modules.crm.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.service.GoodsOrderImeService;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private GoodsOrderImeService goodsOrderImeService;

    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;

    @Autowired
    private RedisTemplate redisTemplate;



    @RequestMapping(method = RequestMethod.GET)
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        Page<GoodsOrderDto> page = goodsOrderService.findPage(pageable, goodsOrderQuery);
        return page;
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme(GoodsOrder goodsOrder, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty(GoodsOrder goodsOrder) {
        return null;
    }



    @RequestMapping(value = "getQuery")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        GoodsOrderQuery result = new GoodsOrderQuery();
        result.setNetTypeList(NetTypeEnum.getList());
        result.setShipTypeList(ShipTypeEnum.getList());
        result.setStatusList(GoodsOrderStatusEnum.getList());
        result.setCreatedDateRange(getThisMonthDateRange());

        return result;
    }


    private String getThisMonthDateRange(){
        LocalDateTime now = LocalDateTime.now();
        LocalDate dateStart = LocalDateTimeUtils.getFirstDayOfMonth(now).toLocalDate();
        return LocalDateUtils.format(dateStart) + " - "+LocalDateUtils.format(now.toLocalDate()) ;
    }

    @RequestMapping(value = "findByFormatId", method = RequestMethod.GET)
    public Object findByFormatId(String formatId) {
        return null;}

//    @RequestMapping(value = "getViewInDetailForm")
//    public GoodsOrderViewInDetailForm getViewInDetailForm(String id) {
//        return goodsOrderService.getViewInDetailForm(id);
//    }

    @RequestMapping(value = "shipForm", method = RequestMethod.GET)
    public String shipForm(String id) {
        return null;
    }

    @RequestMapping(value = "ship", method = RequestMethod.POST)
    public RestResponse shipSave(GoodsOrder goodsOrder, String expressCodes, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "print")
    public String print(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "shipPrint")
    public String shipPrint(GoodsOrder goodsOrder) {
        return null;   }

    @RequestMapping(value = "sign", method = RequestMethod.GET)
    public String signForm(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public RestResponse sign(GoodsOrder goodsOrde) {
        return null;
    }

    @RequestMapping(value = "shipBack", method = RequestMethod.GET)
    public RestResponse reship(GoodsOrder goodsOrder, RedirectAttributes redirectAttributes) {
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(GoodsOrder goodsOrder, RedirectAttributes redirectAttributes) {
        return null;
    }


    @RequestMapping(value = "sreturnFormProperty", method = RequestMethod.GET)
    public String sreturn() {
        return null;
    }

    @RequestMapping(value = "sreturn", method = RequestMethod.GET)
    public String sreturnForm(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "sreturn", method = RequestMethod.POST)
    public RestResponse sreturn(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "billChange", method = RequestMethod.GET)
    public String billChange(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "bill")
    public RestResponse bill(GoodsOrderBillForm goodsOrderBillForm) {
        //TODO 檢查狀態和用戶權限
//        if (!GoodsOrderStatusEnum.待开单.toString().equals(goodsOrderBillForm.getStatus())) {
//            return new Message("message_goods_order_order_not_bill", Message.Type.danger);
//        }
//        // 检查用户
//        if (goodsOrderBillForm.getSyn()) {
//            if (StringUtils.isBlank(RequestUtils.getOutId())) {
//                return new Message("message_goods_order_correct_finance", Message.Type.danger);
//            }
//        }

        goodsOrderService.bill(goodsOrderBillForm);
        return new RestResponse("message_goods_order_bill_success", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(GoodsOrderForm goodsOrderForm) {

        RestResponse restResponse = goodsOrderService.validateShop(goodsOrderForm.getId(), goodsOrderForm.getShopId());
        if(!restResponse.getSuccess()){
            return restResponse;
        }

        goodsOrderService.save(goodsOrderForm);
        return new RestResponse("message_goods_order_product_save_success", ResponseCodeEnum.saved.name());


    }

    @RequestMapping(value = "getForm")
    public GoodsOrderForm getForm(GoodsOrderForm goodsOrderForm) {

        goodsOrderForm.setShipTypeList(ShipTypeEnum.getList());
        //TODO 需要修改判斷公司類別
        goodsOrderForm.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//TODO 判斷公司類別
//        if(CompanyNameEnum.JXOPPO.name().equals(RequestUtils.getCompanyId().getCompany().getName()) || Company.CompanyName.JXvivo.name().equals(AccountUtils.getCompany().getName()) ){
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//        }else{
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.全网通.name()));
//        }

        return goodsOrderForm;
//        return goodsOrderService.getForm(goodsOrderForm);

    }

    @RequestMapping(value = "findOne")
    public GoodsOrderDto findOne(String id) {

        if(StringUtils.isBlank(id)){
            return new GoodsOrderDto();
        }

        return goodsOrderService.findOne(id);

    }

    @RequestMapping(value = "getBillForm")
    public GoodsOrderBillForm getBillForm(GoodsOrderBillForm goodsOrderBillForm) {

        GoodsOrderDto goodsOrderDto = goodsOrderService.findOne(goodsOrderBillForm.getId());
        //设置界面默认值
        goodsOrderBillForm.setBillDate(LocalDate.now());
        goodsOrderBillForm.setSyn(Boolean.TRUE);
        goodsOrderBillForm.setExpressCompanyList(expressCompanyService.findDtoListByCompanyIdAndExpressType(RequestUtils.getCompanyId(), ExpressCompanyTypeEnum.手机订单.name()));
        goodsOrderBillForm.setStoreList(depotService.findStoreList(goodsOrderDto.getShipType()));

        CompanyConfigCacheDto notDepotStoreIdCache = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.IS_NOT_DEPOSIT_STORE.name());
        if(notDepotStoreIdCache!=null && StringUtils.isNotBlank(notDepotStoreIdCache.getValue())){
            goodsOrderBillForm.setNotDepotStoreIdList(IdUtils.getIdList(notDepotStoreIdCache.getValue()));
        }

        return goodsOrderBillForm;
//        return goodsOrderService.getBillForm(goodsOrderBillForm);

    }

    @RequestMapping(value = "findDtoListByGoodsOrderIdForView")
    public List<GoodsOrderDetailDto> findDtoListByGoodsOrderIdForView(String goodsOrderId) {

        return goodsOrderService.findDtoListByGoodsOrderIdForView(goodsOrderId);

    }


    @RequestMapping(value = "findGoodsOrderImeDtoListByGoodsOrderId")
    public List<GoodsOrderImeDto> findGoodsOrderImeDtoListByGoodsOrderId(String goodsOrderId) {

        return goodsOrderImeService.findDtoListByGoodsOrderId(goodsOrderId);

    }


    @RequestMapping(value = "findDetailListForNew")
    public List<GoodsOrderDetailDto> findDetailListForNew(String shopId, String netType) {
        return goodsOrderService.findDetailListForNew(shopId, netType);
    }


    @RequestMapping(value = "findDetailListForEdit")
    public List<GoodsOrderDetailDto> findDetailListForEdit(String id) {
        return goodsOrderService.findDetailListForEdit(id);
    }

    @RequestMapping(value = "findDetailListForBill")
    public List<GoodsOrderDetailDto> findDetailListForBill(String id, String storeId) {
        return goodsOrderService.findDetailListForBill(id, storeId);
    }



    @RequestMapping(value = "findShopByGoodsOrderId")
    public DepotDto findShopByGoodsOrderId(String goodsOrderId) {
        if(StringUtils.isBlank(goodsOrderId)){
            return new DepotDto();
        }
        return goodsOrderService.findShopByGoodsOrderId(goodsOrderId);
    }

    @RequestMapping(value = "findStoreByGoodsOrderId")
    public DepotDto findStoreByGoodsOrderId(String goodsOrderId) {
        if(StringUtils.isBlank(goodsOrderId)){
            return new DepotDto();
        }
        return goodsOrderService.findStoreByGoodsOrderId(goodsOrderId);
    }


}

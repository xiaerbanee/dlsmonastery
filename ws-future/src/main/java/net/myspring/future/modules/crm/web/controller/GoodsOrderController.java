package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;



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
        result.setCreatedDateRange(LocalDateTimeUtils.getThisMonthDateRange());

        return result;
    }

    @RequestMapping(value = "findByFormatId", method = RequestMethod.GET)
    public Object findByFormatId(String formatId) {
        return null;}

    @RequestMapping(value = "detail")
    public String detail(GoodsOrder goodsOrder) {
        return null;
    }

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

    @RequestMapping(value = "getBillForm")
    public GoodsOrderBillForm getBillForm(String id, String storeId) {
        return goodsOrderService.getBillForm(id, storeId);

    }

    @RequestMapping(value = "billSave")
    public RestResponse billSave(GoodsOrderBillForm goodsOrderBillForm) {
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

        goodsOrderService.billSave(goodsOrderBillForm);
        return new RestResponse("message_goods_order_bill_success", ResponseCodeEnum.saved.name());

    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(GoodsOrderForm goodsOrderForm) {

        if (!goodsOrderForm.isCreate() && !GoodsOrderStatusEnum.待开单.name().equals(goodsOrderForm.getStatus()) ) {
            return new RestResponse("message_goods_order_update_fail", ResponseCodeEnum.invalid.name());
        } else {
            goodsOrderService.save(goodsOrderForm);

        }
        return new RestResponse("message_goods_order_product_save_success", ResponseCodeEnum.saved.name());


    }

    @RequestMapping(value = "findForm")
    public GoodsOrderForm findForm(GoodsOrderForm goodsOrderForm) {

        return goodsOrderService.findForm(goodsOrderForm);

    }

}

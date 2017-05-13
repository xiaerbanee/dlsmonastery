package net.myspring.future.modules.crm.web.controller;


import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderService;
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
import sun.misc.Request;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private DepotManager depotManager;
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

    @RequestMapping(value = "getGoodsOrderDetail", method = RequestMethod.GET)
    public String getGoodsOrderDetail(GoodsOrder goodsOrder, BindingResult bindingResult) {
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
        return null;  }

    @RequestMapping(value = "ship", method = RequestMethod.GET)
    public String ship(GoodsOrder goodsOrder) {
        return null;  }

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

    @RequestMapping(value = "bill", method = RequestMethod.GET)
    public String bill(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "bill", method = RequestMethod.POST)
    public RestResponse billSave(GoodsOrder goodsOrder, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(GoodsOrder goodsOrder) {
        return null;
    }

    @RequestMapping(value = "findForm")
    public GoodsOrderForm findForm(GoodsOrderForm goodsOrderForm) {

        return goodsOrderService.findForm(goodsOrderForm);

    }


    private List<String> getActionList(GoodsOrder goodsOrder) {
        return null;
    }
}

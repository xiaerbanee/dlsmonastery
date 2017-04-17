package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.GoodsOrder;

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



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
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


    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery() {
        return null;  }

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

    @RequestMapping(value = "findOne")
    public String findOne(GoodsOrder goodsOrder) {
        return null;
    }


    private List<String> getActionList(GoodsOrder goodsOrder) {
        return null;
    }
}

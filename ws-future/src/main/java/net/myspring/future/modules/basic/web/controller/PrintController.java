package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.service.PrintService;
import net.myspring.future.modules.basic.web.form.PrintConfigForm;
import net.myspring.future.modules.basic.web.query.PrintConfigQuery;
import net.myspring.future.modules.crm.dto.ExpressOrderPrintDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "basic/print")
public class PrintController {

    @Autowired
    private PrintService printService;

    @RequestMapping(value = "checkConfig")
    public Map<String,String> checkConfig(PrintConfigForm printConfigForm) {
        return printService.checkConfig(printConfigForm);
    }

    @RequestMapping(value = "orderList")
    public List<ExpressOrderPrintDto> orderList(PrintConfigQuery printConfigQuery) {
        return printService.findOrderList(printConfigQuery);
    }

    //快递打印
    @RequestMapping(value = "print")
    public Map<String,Object> print(String expressOrderId) {
        printService.print(expressOrderId);
        Map<String,Object> map = Maps.newHashMap();
        map.put("success",true);
        return map;
    }
}

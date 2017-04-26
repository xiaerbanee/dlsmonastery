package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.modules.input.service.ArReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "input/arReceivable")
public class ArReceivableController {
    @Autowired
    private ArReceivableService arReceivableService;

    @RequestMapping(value = "getBillNo", method = RequestMethod.GET)
    public String getBillNo(String outStockBillNo){
        return arReceivableService.findFBillNoByfSourceBillNo(outStockBillNo);
    }
}

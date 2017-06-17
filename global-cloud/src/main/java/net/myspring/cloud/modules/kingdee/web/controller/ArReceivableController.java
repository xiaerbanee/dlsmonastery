package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.ArReceivable;
import net.myspring.cloud.modules.kingdee.service.ArReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下推应收单
 * Created by lihx on 2017/6/17.
 */
@RestController
@RequestMapping(value = "kingdee/arReceivable")
public class ArReceivableController {
    @Autowired
    private ArReceivableService arReceivableService;

    @RequestMapping(value = "findTopOneBySourceBillNo")
    public ArReceivable findTopOneBySourceBillNo(String sourceBillNo){
        return arReceivableService.findTopOneBySourceBillNo(sourceBillNo);
    }
}

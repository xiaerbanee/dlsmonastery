package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.dto.AfterSaleStoreAllotDto;
import net.myspring.future.modules.crm.service.AfterSaleProductAllotService;
import net.myspring.future.modules.crm.service.AfterSaleStoreAllotService;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleStoreAllotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhucc on 2017/7/4.
 */
@RestController
@RequestMapping("crm/afterSaleStoreAllot")
public class AfterSaleStoreAllotController {

    @Autowired
    private AfterSaleStoreAllotService afterSaleStoreAllotService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleStoreAllotDto> list(Pageable pageable, AfterSaleStoreAllotQuery afterSaleStoreAllotQuery) {
        Page<AfterSaleStoreAllotDto> page = afterSaleStoreAllotService.findPage(pageable, afterSaleStoreAllotQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public AfterSaleProductAllotQuery  getQuery(AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        return afterSaleProductAllotQuery;
    }
}

package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.service.AfterSaleProductAllotService;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
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
@RequestMapping("crm/afterSaleProductAllot")
public class AfterSaleProductAllotController  {

    @Autowired
    private AfterSaleProductAllotService afterSaleProductAllotService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleProductAllotDto> list(Pageable pageable, AfterSaleProductAllotQuery afterSaleProductAllotQuery) {
        Page<AfterSaleProductAllotDto> page = afterSaleProductAllotService.findPage(pageable, afterSaleProductAllotQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public AfterSaleProductAllotQuery  getQuery(AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        return afterSaleProductAllotQuery;
    }

}

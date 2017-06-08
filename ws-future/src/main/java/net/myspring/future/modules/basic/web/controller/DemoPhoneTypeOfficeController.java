package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;
import net.myspring.future.modules.basic.service.DemoPhoneTypeOfficeService;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "basic/demoPhoneTypeOffice")
public class DemoPhoneTypeOfficeController {

    @Autowired
    private DemoPhoneTypeOfficeService demoPhoneTypeOfficeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DemoPhoneTypeOfficeDto> list(Pageable pageable, DemoPhoneTypeOfficeQuery demoPhoneTypeOfficeQuery){
        Page<DemoPhoneTypeOfficeDto> page = demoPhoneTypeOfficeService.findPage(pageable,demoPhoneTypeOfficeQuery);
        return page;
    }

    @RequestMapping(value = "/getQuery")
    public DemoPhoneTypeOfficeQuery getQuery(DemoPhoneTypeOfficeQuery demoPhoneTypeOfficeQuery){
        return demoPhoneTypeOfficeQuery;
    }

}

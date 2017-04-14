package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.service.AdPricesystemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "crm/adPricesystemDetail")
public class AdPricesystemDetailController {

    @Autowired
    private AdPricesystemDetailService adPricesystemDetailService;

}

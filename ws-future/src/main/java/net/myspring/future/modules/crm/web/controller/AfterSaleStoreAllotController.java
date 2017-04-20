package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.AfterSaleStoreAllot;
import net.myspring.future.modules.crm.service.AfterSaleStoreAllotService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "crm/afterSaleStoreAllot")
public class AfterSaleStoreAllotController {

    @Autowired
    private AfterSaleStoreAllotService afterSaleStoreAllotService;

    @ModelAttribute
    public AfterSaleStoreAllot get(@RequestParam(required = false) String id) {
        return null;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

}

package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.ImeSaleReport;
import net.myspring.future.modules.crm.service.ImeSaleReportService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "crm/imeSaleReport")
public class ImeSaleReportController {

    @Autowired
    private ImeSaleReportService imeSaleReportService;

    @ModelAttribute
    public ImeSaleReport get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(value = "officeInventory")
    public String findOfficeInventory(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "productInventory")
    public String findProductInventory(HttpServletRequest request) {
        return null;
    }

}

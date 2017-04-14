package net.myspring.future.modules.crm.web.controller;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopDeposit")
public class ShopDepositController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }

    @RequestMapping(value = "getDepartmentByShopId")
    public String getDepartmentByShopId() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete() {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne() {
        return null;
    }

    @RequestMapping(value = "save")
    public String save() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }

}

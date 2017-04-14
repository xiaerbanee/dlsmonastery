package net.myspring.future.modules.crm.web.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopGoodsDeposit")
public class ShopGoodsDepositController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }


    @RequestMapping(value = "getFormProperty")
    public String form() {
        return null;
    }
    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        return null;
    }


    @RequestMapping(value = "searchDepartMent")
    public String searchDepartMent() {
        return null;
    }

    @RequestMapping(value = "save")
    public String save() {
        return null;
    }


    @RequestMapping(value = "audit")
    @ResponseBody
    public String batchSave() {
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

    private List<String> getActionList() {
        return null;
    }


}

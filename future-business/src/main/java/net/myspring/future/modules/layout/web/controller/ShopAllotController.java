package net.myspring.future.modules.layout.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAllot")
public class ShopAllotController {

    @RequestMapping(method = RequestMethod.GET)
    public String list( ){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete() {
        return null;
    }

    @RequestMapping(value = "save")
    public String save() {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit() {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne() {
        return null;
    }

    @RequestMapping(value="getProducts")
    public String getProducts() {
        return null;
    }

    @RequestMapping(value="getEditFormData")
    public String getEditFormData() {
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }

}

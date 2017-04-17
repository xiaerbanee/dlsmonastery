package net.myspring.future.modules.layout.web.controller;


import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopImage")
public class ShopImageController {



    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail() {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save() {
        return null;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String logicDelete() {
        return null;
    }

    public List<String> getActionList() {
        return null;
    }

}

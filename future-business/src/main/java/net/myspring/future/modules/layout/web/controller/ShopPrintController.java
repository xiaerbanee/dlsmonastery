package net.myspring.future.modules.layout.web.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopPrint")
public class ShopPrintController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save( ) {
        return null;
    }

    @RequestMapping(value="getListProperty",method = RequestMethod.GET)
    public String getListProperty() {
        return null;
    }

    @RequestMapping(value="getFormProperty",method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail() {
        return null;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}

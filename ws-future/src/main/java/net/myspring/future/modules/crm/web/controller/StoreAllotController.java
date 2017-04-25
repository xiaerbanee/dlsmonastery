package net.myspring.future.modules.crm.web.controller;



import net.myspring.common.response.RestResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/storeAllot")
public class StoreAllotController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }

    @RequestMapping(value = "save")
    public RestResponse save() {
        return null;
    }

    @RequestMapping(value = "findForm")
    public String findOne() {
        return null;
    }

    @RequestMapping(value = "getStoreAllotData")
    public String getStoreAllotData() {
        return null;
    }

    @RequestMapping(value = "getCloudQty")
    public String getCloudQty() {
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value="getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.GET)
    public String shipForm() {
        return null;
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.POST)
    public RestResponse ship() {
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }

}

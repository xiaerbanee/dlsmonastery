package net.myspring.future.modules.layout.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopBuild")
public class ShopBuildController {


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save() {
        return null;
    }

    @RequestMapping(value = "batchAudit", method = RequestMethod.GET)
    public String batchAudit() {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete() {
        return null;
    }
    @RequestMapping(value = "detail")
    public String detail() {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}

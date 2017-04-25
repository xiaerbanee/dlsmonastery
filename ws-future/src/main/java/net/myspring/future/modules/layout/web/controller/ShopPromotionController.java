package net.myspring.future.modules.layout.web.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopPromotion")
public class ShopPromotionController {

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return null;
   }

    @RequestMapping(value = "findForm")
    public String findOne() {
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

    @RequestMapping(value="save")
    public String save() {
        return null;
    }

    @RequestMapping(value="detail")
    public String detail() {
        return null;
    }

    @RequestMapping(value="delete")
    @ResponseBody
    public String delete() {
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}

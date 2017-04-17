package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.service.AdPricesystemService;
import net.myspring.future.modules.basic.service.DepotService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/adPricesystem")
public class AdPricesystemController {

    @Autowired
    private AdPricesystemService adPricesystemService;
    @Autowired
    private DepotService depotService;

    @ModelAttribute
    public AdPricesystem get(@RequestParam(required = false) String id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value = "save")
    public String save(AdPricesystem adPricesystem){
        adPricesystemService.save(adPricesystem);
        return null;

    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(AdPricesystem adPricesystem){
        Map<String,Object> map = Maps.newHashMap();
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(AdPricesystem adPricesystem, BindingResult bindingResult) {

        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(AdPricesystem adPricesystem){
        return null;
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();

        return actionList;
    }
}

package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.Pricesystem;
import net.myspring.future.modules.crm.service.PricesystemService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/pricesystem")
public class PricesystemController {

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(Pricesystem pricesystem) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(  Pricesystem pricesystem) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(Pricesystem pricesystem){
        return null;
    }

    private List<String> getActionList() {
        return null;
    }

}

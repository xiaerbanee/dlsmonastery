package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "crm/depotChange")
public class DepotChangeController {


    @RequestMapping(method = RequestMethod.GET)
    public String list( ){
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        return null;
    }


    @RequestMapping(value = "save")
    public RestResponse save( ) {
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete( ) {
        return null;
    }

    @RequestMapping(value = "getForm")
    public String getForm() {
        return null;
    }

    @RequestMapping(value = "getQuery")
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( ) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit( ) {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export( ) {
        return null;
    }

    private List<String> getActionList( ) {
        List<String> actionList = Lists.newArrayList();
        return null;
    }
}

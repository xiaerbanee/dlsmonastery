package net.myspring.future.modules.basic.web.controller;


import net.myspring.common.response.RestResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/dealer")
public class DealerController {



    @RequestMapping(method= RequestMethod.GET)
    public String list( ){
        return null;
    }

    @RequestMapping(value = "save")
    public RestResponse save( ) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne( ){
        return null;
    }

    private List<String> getActionList( ) {
        return null;
    }
}

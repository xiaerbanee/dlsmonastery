package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/chain")
public class ChainController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){

        return null;
    }

    @RequestMapping(value = "delete")
    public String delete() {

        return null;
    }

    @RequestMapping(value = "save")
    public String save( ) {

        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne( ){ return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        return null;
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        return null;
    }

    private List<String> getActionList() {
        return null;
    }
}

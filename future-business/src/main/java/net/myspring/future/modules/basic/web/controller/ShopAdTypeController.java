package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.myspring.future.modules.basic.domain.ShopAdType;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAdType")
public class ShopAdTypeController {


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }
    @RequestMapping(value = "delete")
    public String delete(ShopAdType shopAdType) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(ShopAdType shopAdType) {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopAdType shopAdType){
        return null;
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

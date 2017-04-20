package net.myspring.future.modules.crm.web.controller;



import net.myspring.future.modules.crm.domain.ImeAllot;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/imeAllot")
public class ImeAllotController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }
    @RequestMapping(value = "detail")
    public String detail(ImeAllot imeAllot) {
        return null;
    }

    @RequestMapping(value="getQuery")
    public String form(){
        return null;
    }
    @RequestMapping(value = "delete")
    public String delete(String id) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(ImeAllot imeAllot, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        return null;
    }

    @RequestMapping(value = "searchImeMap")
    public String searchImeMap(String imeStr){
        return null;
    }

    private List<String> getActionList(ImeAllot imeAllot) {
        return null;
    }
}

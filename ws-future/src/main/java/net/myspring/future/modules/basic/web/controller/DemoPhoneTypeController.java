package net.myspring.future.modules.basic.web.controller;

import net.myspring.future.modules.basic.domain.DemoPhoneType;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "basic/demoPhoneType")
public class DemoPhoneTypeController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String form() {
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(DemoPhoneType demoPhoneType){
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(DemoPhoneType demoPhoneType){
        return null;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( DemoPhoneType demoPhoneType){
        return null;
    }


    @RequestMapping(value = "delete")
    public String delete(DemoPhoneType demoPhoneType, RedirectAttributes redirectAttributes) {
        return null;
    }

    private List<String> getActionList(DemoPhoneType DemoPhoneType) {
        return null;
    }
}

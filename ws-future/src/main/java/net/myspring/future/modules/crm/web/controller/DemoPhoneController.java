package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.domain.DemoPhone;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/demoPhone")
public class DemoPhoneController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String form(Model model) {
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save( DemoPhone demoPhone){
        return null;
    }

    @RequestMapping(value = "findForm")
    public String findOne(DemoPhone demoPhone){
        return null;
    }

    @RequestMapping(value = "delete")
    public String delete(DemoPhone demoPhone, RedirectAttributes redirectAttributes) {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        return null;
    }
    private List<String> getActionList(DemoPhone demoPhone) {
        return null;
    }
}

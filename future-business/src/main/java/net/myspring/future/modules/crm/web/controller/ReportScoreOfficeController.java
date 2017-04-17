package net.myspring.future.modules.crm.web.controller;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScoreOffice")
public class ReportScoreOfficeController {



    @RequestMapping
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    @ResponseBody
    public String getQuery(){
        return null;
    }
}

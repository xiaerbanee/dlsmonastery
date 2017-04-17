package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.model.AdApplyGoodsModel;
import net.myspring.future.modules.crm.model.AdApplyModel;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "crm/adApply")
public class AdApplyController {


    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public String getQuery() {
        return null;
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getAdApplyList", method = RequestMethod.GET)
    public String form(String billType,String shopId){
        return null;
    }

    @RequestMapping(value = "getAdApplyGoodsList", method = RequestMethod.GET)
    public String form(){
        return null;
    }

    @RequestMapping(value = "getBillFormProperty", method = RequestMethod.GET)
    public String getBillFormProperty(String billType){
        return null;
    }

    @RequestMapping(value = "getBillAdApplyMap", method = RequestMethod.GET)
    public String getBillAdApplyMap(String billType){
        return null;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(AdApplyModel adApplyModel){
        return null;
    }

    @RequestMapping(value = "goodsSave",method = RequestMethod.POST)
    public String goodsSave(AdApplyGoodsModel adApplyGoodsModel){
        return null;
    }

    @RequestMapping(value = "billSave", method = RequestMethod.POST)
    public RestResponse billSave(AdApplyModel adApplyModel) {
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        return null;
    }
}

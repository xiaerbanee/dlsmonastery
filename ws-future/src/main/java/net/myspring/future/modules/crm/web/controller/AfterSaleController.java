package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.service.AfterSaleService;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/afterSale")
public class AfterSaleController {


    @Autowired
    private AfterSaleService afterSaleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleDto> list(Pageable pageable, AfterSaleQuery afterSaleQuery) {
        Page<AfterSaleDto> page = afterSaleService.findPage(pageable, afterSaleQuery);
        return page;
    }

    @RequestMapping(value = "getFromCompanyData",method = RequestMethod.GET)
    public String getFromCompanyData(HttpServletRequest request) {

        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getQuery(){
        Map<String,Object> map= Maps.newHashMap();

        return null;
    }

    @RequestMapping(value = "formData", method = RequestMethod.GET)
    public String formData(String imeStr) {

        return null;
    }

    @RequestMapping(value = "editFormData", method = RequestMethod.GET)
    public String editFormData(String imeStr) {

        return null;
    }

    @RequestMapping(value="searchImeMap" ,method=RequestMethod.GET)
    public String searchImeMap(String imeStr){

        return null;
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(String data, String toStoreDate) {

        return null;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public RestResponse update(String data) {

        return null;
    }

    @RequestMapping(value = "fromCompany", method = RequestMethod.POST)
    public RestResponse fromCompany(String data,String fromCompanyDate) {

        return null;
    }

    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    public RestResponse toCompany(String imeStr,AfterSale afterSale) {

        return null;
    }

    @RequestMapping(value = "toCompanyForm")
    public String toCompanyForm(String imeStr) {
        Map<String,Object> map=Maps.newHashMap();
        StringBuilder stringBuilder=new StringBuilder();

        return null;
    }


    @RequestMapping(value = "synToFinance")
    public String synToFinance() {

        return null;
    }

    @RequestMapping(value = "delete")
    public String logicDelete(String id) {

        return null;
    }

    public List<String> getActionList(AfterSale afterSale){
        List<String> actionList = Lists.newArrayList();

        return null;
    }

}

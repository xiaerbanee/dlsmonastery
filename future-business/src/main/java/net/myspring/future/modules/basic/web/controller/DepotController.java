package net.myspring.future.modules.basic.web.controller;


import net.myspring.future.modules.basic.domain.Depot;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/depot")
public class DepotController {



    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "detail")
    public String detail(Depot depot) {
        return null;
    }

    @RequestMapping(value = "inventory")
    public String inventory(HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "inventoryDetail")
    public String inventoryDetail(Depot depot, HttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "save")
    public String save(Depot depot, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(value = "syn")
    public String syn(){
        return null;
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        return null;
    }

    @RequestMapping(value = "findOne")
    public String findOne(Depot depot){
        return null;
    }

    @RequestMapping(value = "search")
    public String search(String name,String category) {
        return null;
    }

    @RequestMapping(value = "proxyShop")
    public String adShop(String key) {
        return null;
    }

    @RequestMapping(value = "adShop")
    public String adShop(String adShopName,String billType){
        return null;
    }

    @RequestMapping(value = "adShopBsc")
    public String adShopBsc(String key) {
        return null;
    }

    @RequestMapping(value = "shop")
    public String shop(String name) {
        return null;
    }

    private List<String> getInventoryActionList(Depot depot){
        return null;    }

    private List<String> getActionList(Depot depot) {
        return null;
    }

    @RequestMapping(value = "depotAccountData",method = RequestMethod.GET)
    public String depotAccountData(HttpServletRequest request){
        return null;
    }

    @RequestMapping(value = "depotAccountDetail",method = RequestMethod.GET)
    public String depotAccountDetail(Depot depot, HttpServletRequest request) {
        return null;
    }

    //导出所有直营门店的应收
    @RequestMapping(value = "shopExport", method = RequestMethod.GET)
    public ModelAndView shopExport(HttpServletRequest request) {
        return null;
    }

    //导出应收(明细)
    @RequestMapping(value = "accountExport", method = RequestMethod.GET)
    public ModelAndView accountExport(HttpServletRequest request) {
        return null;}
}

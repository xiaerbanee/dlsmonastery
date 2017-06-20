package net.myspring.future.modules.third.web;


import net.myspring.future.modules.third.domain.*;
import net.myspring.future.modules.third.service.OppoService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "third/factory/oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;

    @RequestMapping(value="synIme")
    public void synIme(String date){
        oppoService.synIme(date);
    }

    @RequestMapping(value="findOppoCustomerAllots")
    @ResponseBody
    public String findOppoCustomerAllots(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoService.findOppoCustomerAllots(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateStart),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerAllots);
    }

    @RequestMapping(value="findOppoCustomerStocks")
    @ResponseBody
    public  String findOppoCustomerStocks(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerStock> oppoCustomerStocks=oppoService.findOppoCustomerStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerStocks);
    }


    @RequestMapping(value="findOppoCustomerImeiStocks")
    @ResponseBody
    public  String findOppoCustomerImeiStocks(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerImeiStock> oppoCustomerStocks=oppoService.findOppoCustomerImeiStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerStocks);
    }

    @RequestMapping(value="findOppoCustomerSales")
    @ResponseBody
    public  String findOppoCustomerSales(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSale> oppoCustomerSales=oppoService.findOppoCustomerSales(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSales);
    }

    @RequestMapping(value="findOppoCustomerSaleImes")
    @ResponseBody
    public  String findOppoCustomerSaleImes(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSaleImei> oppoCustomerSaleImes=oppoService.findOppoCustomerSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSaleImes);
    }

    @RequestMapping(value="findOppoCustomerSaleCounts")
    @ResponseBody
    public  String findOppoCustomerSaleCounts(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoService.findOppoCustomerSaleCounts(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSaleCounts);
    }

    @RequestMapping(value="findOppoCustomerAfterSaleImeis")
    @ResponseBody
    public  String findOppoCustomerAfterSaleImeis(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoService.findOppoCustomerAfterSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerAfterSaleImeis);
    }

    @RequestMapping(value="findOppoCustomerDemoPhones")
    @ResponseBody
    public  String findOppoCustomerDemoPhones(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoService.findOppoCustomerDemoPhones(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerDemoPhones);
    }

}

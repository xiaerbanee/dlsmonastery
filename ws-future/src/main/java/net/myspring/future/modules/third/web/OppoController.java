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
    public String synIme(String date){
        return oppoService.synIme(date);
    }

    @RequestMapping(value="findOppoCustomerAllots")
    public List<OppoCustomerAllot> findOppoCustomerAllots(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoService.findOppoCustomerAllots(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        for(OppoCustomerAllot oppoCustomerAllot:oppoCustomerAllots){
            System.err.println("oppoCustomerAllot=="+oppoCustomerAllot.getFromCustomerid()+"\t"+oppoCustomerAllot.getToCustomerid()+"\t"+oppoCustomerAllot.getProductcode()+"\t"+oppoCustomerAllot.getDate()+"\t"+oppoCustomerAllot.getQty());
        }
        return oppoCustomerAllots;
    }

    @RequestMapping(value="findOppoCustomerStocks")
    public  String findOppoCustomerStocks(String dateStart,String dateEnd,String companyId){

        List<OppoCustomerStock> oppoCustomerStocks=oppoService.findOppoCustomerStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerStocks);
    }


    @RequestMapping(value="findOppoCustomerImeiStocks")
    public  String findOppoCustomerImeiStocks(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerImeiStock> oppoCustomerStocks=oppoService.findOppoCustomerImeiStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerStocks);
    }

    @RequestMapping(value="findOppoCustomerSales")
    public  String findOppoCustomerSales(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSale> oppoCustomerSales=oppoService.findOppoCustomerSales(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSales);
    }

    @RequestMapping(value="findOppoCustomerSaleImes")
    public  String findOppoCustomerSaleImes(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSaleImei> oppoCustomerSaleImes=oppoService.findOppoCustomerSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSaleImes);
    }

    @RequestMapping(value="findOppoCustomerSaleCounts")
    public  String findOppoCustomerSaleCounts(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoService.findOppoCustomerSaleCounts(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerSaleCounts);
    }

    @RequestMapping(value="findOppoCustomerAfterSaleImeis")
    public  String findOppoCustomerAfterSaleImeis(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoService.findOppoCustomerAfterSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerAfterSaleImeis);
    }

    @RequestMapping(value="findOppoCustomerDemoPhones")
    public  String findOppoCustomerDemoPhones(String dateStart,String dateEnd,String companyId){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoService.findOppoCustomerDemoPhones(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd),companyId);
        return ObjectMapperUtils.writeValueAsString(oppoCustomerDemoPhones);
    }

}

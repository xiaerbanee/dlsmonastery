package net.myspring.future.modules.third.web;


import net.myspring.future.common.utils.ScheduleUtils;
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
    @Autowired
    private ScheduleUtils scheduleUtils;

    @RequestMapping(value="synIme")
    public String synIme(String date){
        return scheduleUtils.synOppo(date);
    }

    @RequestMapping(value="findOppoCustomerAllots")
    public List<OppoCustomerAllot> findOppoCustomerAllots(String dateStart,String dateEnd){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoService.findOppoCustomerAllots(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerAllots;
    }

    @RequestMapping(value="findOppoCustomerStocks")
    public  List<OppoCustomerStock> findOppoCustomerStocks(String dateStart,String dateEnd){
        List<OppoCustomerStock> oppoCustomerStocks=oppoService.findOppoCustomerStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerStocks;
    }


    @RequestMapping(value="findOppoCustomerImeiStocks")
    public  List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(String dateStart,String dateEnd){
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoService.findOppoCustomerImeiStocks(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        System.err.println("oppoCustomerImeiStocks==="+oppoCustomerImeiStocks.toString());
        return oppoCustomerImeiStocks;
    }

    @RequestMapping(value="findOppoCustomerSales")
    public  List<OppoCustomerSale> findOppoCustomerSales(String dateStart,String dateEnd){
        List<OppoCustomerSale> oppoCustomerSales=oppoService.findOppoCustomerSales(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerSales;
    }

    @RequestMapping(value="findOppoCustomerSaleImes")
    public  List<OppoCustomerSaleImei> findOppoCustomerSaleImes(String dateStart,String dateEnd){
        List<OppoCustomerSaleImei> oppoCustomerSaleImes=oppoService.findOppoCustomerSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerSaleImes;
    }

    @RequestMapping(value="findOppoCustomerSaleCounts")
    public  List<OppoCustomerSaleCount> findOppoCustomerSaleCounts(String dateStart,String dateEnd){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoService.findOppoCustomerSaleCounts(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerSaleCounts;
    }

    @RequestMapping(value="findOppoCustomerAfterSaleImeis")
    public  List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImeis(String dateStart,String dateEnd){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoService.findOppoCustomerAfterSaleImeis(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerAfterSaleImeis;
    }

    @RequestMapping(value="findOppoCustomerDemoPhones")
    public  List<OppoCustomerDemoPhone> findOppoCustomerDemoPhones(String dateStart,String dateEnd){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoService.findOppoCustomerDemoPhones(LocalDateUtils.parse(dateStart),LocalDateUtils.parse(dateEnd));
        return oppoCustomerDemoPhones;
    }

}

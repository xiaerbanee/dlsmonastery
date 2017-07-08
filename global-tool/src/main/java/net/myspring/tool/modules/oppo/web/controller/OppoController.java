package net.myspring.tool.modules.oppo.web.controller;

import com.netflix.discovery.converters.Auto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.service.OppoPushSerivce;
import net.myspring.tool.modules.oppo.service.OppoService;
import net.myspring.tool.modules.oppo.utils.OppoPullScheduleUtils;
import net.myspring.tool.modules.oppo.utils.OppoPushScheduleUtils;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;
    @Autowired
    private OppoPushSerivce oppoPushSerivce;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OppoPullScheduleUtils oppoPullScheduleUtils;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "pullFactoryData")
    public String pullFactoryData(String date) {
        oppoPullScheduleUtils.synOppo(date);
        return "OPPO同步成功";
    }

    @RequestMapping(value = "pushFactoryData")
    public String pushFactoryData(String date) {
        oppoPushSerivce.getOppoCustomers("M13AMB");
        oppoPushSerivce.getOppoCustomerOperatortype();
        List<OppoCustomerAllot> oppoCustomerAllots=oppoPushSerivce.getFutureOppoCustomerAllot(date);
        oppoPushSerivce.getOppoCustomerAllot(oppoCustomerAllots);
        List<OppoCustomerStock> oppoCustomerStocks=oppoPushSerivce.getFutureOppoCustomerStock(date);
        oppoPushSerivce.getOppoCustomerStock(oppoCustomerStocks);
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoPushSerivce.getFutureOppoCustomerImeiStock(date);
        oppoPushSerivce.getOppoCustomerImeiStock(oppoCustomerImeiStocks);
        List<OppoCustomerSale> oppoCustomerSales=oppoPushSerivce.getFutureOppoCustomerSale(date);
        oppoPushSerivce.getOppoCustomerSales(oppoCustomerSales);
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoPushSerivce.getFutureOppoCustomerSaleImeis(date);
        oppoPushSerivce.getOppoCustomerSaleImes(oppoCustomerSaleImeis);
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoPushSerivce.getFutureOppoCustomerSaleCounts(date);
        oppoPushSerivce.getOppoCustomerSaleCounts(oppoCustomerSaleCounts);
        List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis=oppoPushSerivce.getFutureOppoCustomerAfterSaleImeis(date);
        oppoPushSerivce.getOppoCustomerAfterSaleImeis(oppoCustomerAfterSaleImeis);
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoPushSerivce.getFutureOppoCustomerDemoPhone(date);
        oppoPushSerivce.getOppoCustomerDemoPhone(oppoCustomerDemoPhones);


        return "OPPO同步成功";
    }

    @RequestMapping(value = "synIme")
    public List<OppoPlantSendImeiPpselDto> synIme(String date,String agentCode) {
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtos = oppoService.synIme(date,agentCode);
        return oppoPlantSendImeiPpselDtos;
    }

    @RequestMapping(value = "synProductItemelectronSel")
    public List<OppoPlantProductItemelectronSel> synProductItemelectronSel(String date,String agentCode) {
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoService.synProductItemelectronSel(date,agentCode);
        return oppoPlantProductItemelectronSels;
    }
}

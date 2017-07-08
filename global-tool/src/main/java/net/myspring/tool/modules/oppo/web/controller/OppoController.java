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
        oppoPushSerivce.getOppoCustomers(date);
        oppoPushSerivce.getOppoCustomerOperatortype(date);
        List<OppoCustomerAllot> oppoCustomerAllots=oppoPushSerivce.getFutureOppoCustomerAllot(date);
        oppoPushSerivce.getOppoCustomerAllot(oppoCustomerAllots,date);
        List<OppoCustomerStock> oppoCustomerStocks=oppoPushSerivce.getFutureOppoCustomerStock(date);
        oppoPushSerivce.getOppoCustomerStock(oppoCustomerStocks,date);
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoPushSerivce.getFutureOppoCustomerImeiStock(date);
        oppoPushSerivce.getOppoCustomerImeiStock(oppoCustomerImeiStocks,date);
        List<OppoCustomerSale> oppoCustomerSales=oppoPushSerivce.getFutureOppoCustomerSale(date);
        oppoPushSerivce.getOppoCustomerSales(oppoCustomerSales,date);
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoPushSerivce.getFutureOppoCustomerSaleImeis(date);
        oppoPushSerivce.getOppoCustomerSaleImes(oppoCustomerSaleImeis,date);
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoPushSerivce.getFutureOppoCustomerSaleCounts(date);
        oppoPushSerivce.getOppoCustomerSaleCounts(oppoCustomerSaleCounts,date);
        List<OppoCustomerAfterSaleImei>  oppoCustomerAfterSaleImeis=oppoPushSerivce.getFutureOppoCustomerAfterSaleImeis(date);
        oppoPushSerivce.getOppoCustomerAfterSaleImeis(oppoCustomerAfterSaleImeis,date);
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoPushSerivce.getFutureOppoCustomerDemoPhone(date);
        oppoPushSerivce.getOppoCustomerDemoPhone(oppoCustomerDemoPhones,date);
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

    //代理商经销商基础数据上抛
    @RequestMapping(value = "pullCustomers", method = RequestMethod.GET)
    public String pullOppoCustomers(String key, String createdDate) {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + createdDate);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            List<OppoCustomer> oppoCustomers = oppoPushSerivce.getOppoCustomersByDate(createdDate);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomers));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //运营商属性上抛
    @RequestMapping(value = "pullCustomerOperatortype", method = RequestMethod.GET)
    public String pullOppoCustomerOperatortype(String key, String createdDate) {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + createdDate);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            List<OppoCustomerOperatortype> oppoCustomerOperatortypes = oppoPushSerivce.getOppoCustomerOperatortypesByDate(createdDate);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerOperatortypes));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //发货退货调拨数据上抛
    @RequestMapping(value ="pullCustomerAllot", method = RequestMethod.GET)
    public String pullOppoCustomersAllot(String key, String dateStart, String dateEnd) {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey=MD5Utils.encode(factoryAgentName+dateStart+dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerAllot> oppoCustomerAllots=oppoPushSerivce.getOppoCustomerAllotsByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerAllots));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //库存数据上抛
    @RequestMapping(value ="pullCustomerStock", method = RequestMethod.GET)
    public String pullCustomerStock(String key,String createdDate)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey=MD5Utils.encode(factoryAgentName+createdDate);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            logger.info("库存上抛开始："+new Date());
            String dateStart= LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusMonths(-12));
            String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
            List<OppoCustomerStock> oppoCustomerStocks=oppoPushSerivce.getOppoCustomerStocksByDate(dateStart,dateEnd);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerStocks));
            responseMessage.setResult("success");
        }
        logger.info("库存上抛结束："+new Date());
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店条码调拨明细上抛
    @RequestMapping(value ="pullCustomerImeStock", method = RequestMethod.GET)
    public String pullCustomerImeStock(String key,String dateStart,String dateEnd)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey=MD5Utils.encode(factoryAgentName+dateStart+dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoPushSerivce.getOppoCustomerImeiStocksByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerImeiStocks));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //店总数据上抛
    @RequestMapping(value ="pullCustomerSale", method = RequestMethod.GET)
    public String pullCustomerSale(String key,String dateStart,String dateEnd) {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerSale> oppoCustomerSales = oppoPushSerivce.getOppoCustomerSalesByDate(dateStartTime, dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSales));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店销售明细
    @RequestMapping(value ="pullCustomerSaleIme", method = RequestMethod.GET)
    public String pullCustomerSaleIme(String key,String dateStart,String dateEnd)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerSaleImei> oppoCustomerSaleImes=oppoPushSerivce.getOppoCustomerSaleImeisByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSaleImes));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店销售数据汇总
    @RequestMapping(value ="pullCustomerSaleCount", method = RequestMethod.GET)
    public String pullCustomerSaleCount(String key,String dateStart,String dateEnd)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            logger.info("门店销售数据汇总上抛开始");
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoPushSerivce.getOppoCustomerSaleCountsByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSaleCounts));
            responseMessage.setResult("success");
            logger.info("门店销售数据汇总上抛结束");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店售后零售退货条码数据
    @RequestMapping(value ="pullCustomerAfterSaleIme", method = RequestMethod.GET)
    public String pullCustomerAfterSaleIme(String key,String dateStart,String dateEnd)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            logger.info("门店店售后退货条码数据上抛开始");
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoPushSerivce.getOppoCustomerAfterSaleImeisByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerAfterSaleImeis));
            responseMessage.setResult("success");
            logger.info("门店店售后退货条码数据上抛结束");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //演示机条码数据
    @RequestMapping(value ="pullCustomerDemoPhone", method = RequestMethod.GET)
    public String pullCustomerDemoPhone(String key,String dateStart,String dateEnd)  {
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            String dateStartTime= LocalDateUtils.format(LocalDateUtils.parse(dateStart));
            String dateEndTime=LocalDateUtils.format(LocalDateUtils.parse(dateEnd).plusDays(1));
            List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoPushSerivce.getOppoCustomerDemoPhonesByDate(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerDemoPhones));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }



}

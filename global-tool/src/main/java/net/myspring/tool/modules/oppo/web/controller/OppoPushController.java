package net.myspring.tool.modules.oppo.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.dto.OppoPlantSendImeiPpselDto;
import net.myspring.tool.modules.oppo.dto.OppoPushDto;
import net.myspring.tool.modules.oppo.dto.OppoResponseMessage;
import net.myspring.tool.modules.oppo.service.OppoPushSerivce;
import net.myspring.tool.modules.oppo.service.OppoPullService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "factory/oppo")
public class OppoPushController {
    @Autowired
    private OppoPushSerivce oppoPushSerivce;
    @Autowired
    private RedisTemplate redisTemplate;


    protected Logger logger = LoggerFactory.getLogger(getClass());


    //将需要上抛的数据先同步到本地数据库
    @RequestMapping(value = "pushToLocal")
    public String pushToLocal(String companyName,String date) {
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        OppoPushDto oppoPushDto = new OppoPushDto();
        oppoPushDto.setDate(date);
        oppoPushDto.setCustomerDtos(oppoPushSerivce.getOppoCustomers());
        oppoPushDto.setOppoCustomerAllots(oppoPushSerivce.getFutureOppoCustomerAllot(date));
        oppoPushDto.setOppoCustomerStocks(oppoPushSerivce.getFutureOppoCustomerStock(date));
        oppoPushDto.setOppoCustomerImeiStocks(oppoPushSerivce.getFutureOppoCustomerImeiStock(date));
        oppoPushDto.setOppoCustomerSales(oppoPushSerivce.getFutureOppoCustomerSale(date));
        oppoPushDto.setOppoCustomerSaleImeis(oppoPushSerivce.getFutureOppoCustomerSaleImeis(date));
        oppoPushDto.setOppoCustomerSaleCounts(oppoPushSerivce.getFutureOppoCustomerSaleCounts(date));
        oppoPushDto.setOppoCustomerAfterSaleImeis(oppoPushSerivce.getFutureOppoCustomerAfterSaleImeis(date));
        oppoPushDto.setOppoCustomerDemoPhones(oppoPushSerivce.getFutureOppoCustomerDemoPhone(date));

        oppoPushSerivce.pushToLocal(oppoPushDto);

        return "OPPO同步成功";
    }

    //代理商经销商基础数据上抛
    @RequestMapping(value = "pullCustomers", method = RequestMethod.GET)
    public OppoResponseMessage pullOppoCustomers(String key, String createdDate) throws IOException {
        String agentCode= CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //运营商属性上抛
    @RequestMapping(value = "pullCustomerOperatortype", method = RequestMethod.GET)
    public OppoResponseMessage pullOppoCustomerOperatortype(String key, String createdDate) {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        logger.info("responseMessage=="+responseMessage.toString());
        return responseMessage;
    }

    //发货退货调拨数据上抛
    @RequestMapping(value ="pullCustomerAllot", method = RequestMethod.GET)
    public OppoResponseMessage pullOppoCustomersAllot(String key, String dateStart, String dateEnd) {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //库存数据上抛
    @RequestMapping(value ="pullCustomerStock", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerStock(String key,String createdDate)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        String factoryAgentName =agentCode.split(CharConstant.COMMA)[0];
        String localKey=MD5Utils.encode(factoryAgentName+createdDate);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            logger.info("库存上抛开始："+new Date());
            String dateStart= LocalDateUtils.format(LocalDateUtils.parse(createdDate));
            String dateEnd=LocalDateUtils.format(LocalDateUtils.parse(createdDate).plusDays(1));
            List<OppoCustomerStock> oppoCustomerStocks=oppoPushSerivce.getOppoCustomerStocksByDate(dateStart,dateEnd);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerStocks));
            responseMessage.setResult("success");
        }
        logger.info("库存上抛结束："+new Date());
        return responseMessage;
    }

    //门店条码调拨明细上抛
    @RequestMapping(value ="pullCustomerImeStock", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerImeStock(String key,String dateStart,String dateEnd)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //店总数据上抛
    @RequestMapping(value ="pullCustomerSale", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerSale(String key,String dateStart,String dateEnd) {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //门店销售明细
    @RequestMapping(value ="pullCustomerSaleIme", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerSaleIme(String key,String dateStart,String dateEnd)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //门店销售数据汇总
    @RequestMapping(value ="pullCustomerSaleCount", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerSaleCount(String key,String dateStart,String dateEnd)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //门店售后零售退货条码数据
    @RequestMapping(value ="pullCustomerAfterSaleIme", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerAfterSaleIme(String key,String dateStart,String dateEnd)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }

    //演示机条码数据
    @RequestMapping(value ="pullCustomerDemoPhone", method = RequestMethod.GET)
    public OppoResponseMessage pullCustomerDemoPhone(String key,String dateStart,String dateEnd)  {
        String agentCode=CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
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
        return responseMessage;
    }
}

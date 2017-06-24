package net.myspring.tool.modules.oppo.web;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.config.JdbcConfig;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.service.OppoPushSerivce;
import net.myspring.tool.modules.oppo.service.OppoService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private RedisTemplate redisTemplate;
    @Autowired
    private OppoPushSerivce oppoPushSerivce;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "syn")
    public String synFactoryOppo(String date) {
        List<String> mainCodes = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue(), CharConstant.COMMA);
        List<String> mainPasswords = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.FACTORY_AGENT_PASSWORDS.name()).getValue(), CharConstant.COMMA);
        if(CollectionUtil.isEmpty(mainCodes)){
            mainCodes.add("M13AMB");
        }
        if(CollectionUtil.isEmpty(mainPasswords)){
            mainPasswords.add("OP098");
        }
        LocalDate localDate = LocalDateUtils.parse(date);
        List<OppoPlantProductSel> oppoPlantProductSels=oppoService.plantProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        //同步颜色编码
        logger.info("开始同步颜色编码");
        oppoService.pullPlantProductSels(oppoPlantProductSels);
        logger.info("开始同步物料编码");
//        同步物料编码
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoService.plantAgentProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        String message = oppoService.pullPlantAgentProductSels(oppoPlantAgentProductSels);
        logger.info("开始同步发货串码");
        //同步发货串吗
        for (int i = 0; i < mainCodes.size(); i++) {
            List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselList = oppoService.plantSendImeiPPSel(mainCodes.get(i), mainPasswords.get(i), date);
            if (CollectionUtil.isNotEmpty(oppoPlantSendImeiPpselList)) {
                oppoService.pullPlantSendImeiPpsels(oppoPlantSendImeiPpselList, mainCodes.get(i));
            }
        }
        //同步电子保卡
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoService.plantProductItemelectronSel(mainCodes.get(0), mainPasswords.get(0), localDate);
        oppoService.pullPlantProductItemelectronSels(oppoPlantProductItemelectronSels);
        return "OPPO同步成功";
    }

    @RequestMapping(value = "synIme")
    public String synIme(String date) {
//        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselDtos = oppoService.synIme(date);
//        return ObjectMapperUtils.writeValueAsString(oppoPlantSendImeiPpselDtos);
        return "";
    }

    //代理商经销商基础数据上抛
    @RequestMapping(value = "pullCustomers", method = RequestMethod.GET)
    public String pullOppoCustomers(String key, String createdDate, HttpServletResponse response, Model model) {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + createdDate);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            List<OppoCustomer> oppoCustomers = oppoPushSerivce.getOppoCustomers();
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomers));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //运营商属性上抛
    @RequestMapping(value = "pullCustomerOperatortype", method = RequestMethod.GET)
    @ResponseBody
    public String pullOppoCustomerOperatortype(String key, String createdDate, HttpServletResponse response, Model model) {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + createdDate);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            List<OppoCustomerOperatortype> oppoCustomerOperatortypes = oppoPushSerivce.getOppoCustomerOperatortype();
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerOperatortypes));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //发货退货调拨数据上抛
    @RequestMapping(value ="pullCustomerAllot", method = RequestMethod.GET)
    @ResponseBody
    public String pullOppoCustomersAllot(String key, String dateStart, String dateEnd, HttpServletResponse response, Model model) {
        String factoryAgentName="M13AMB";
        String localKey=MD5Utils.encode(factoryAgentName+dateStart+dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime= LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime=LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerAllot> oppoCustomerAllots=oppoPushSerivce.getOppoCustomerAllot(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerAllots));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //库存数据上抛
    @RequestMapping(value ="pullCustomerStock", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerStock(String key,String createdDate,HttpServletResponse response, Model model)  {
        String factoryAgentName="M13AMB";
        String localKey=MD5Utils.encode(factoryAgentName+createdDate);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            logger.info("库存上抛开始："+new Date());
            LocalDate dateStart= LocalDateUtils.parse(createdDate);
            LocalDate dateEnd=LocalDateUtils.parse(createdDate).plusDays(1);
            List<OppoCustomerStock> oppoCustomerStocks=oppoPushSerivce.getOppoCustomerStock(dateStart,dateEnd);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerStocks));
            responseMessage.setResult("success");
        }
        logger.info("库存上抛结束："+new Date());
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店条码调拨明细上抛
    @RequestMapping(value ="pullCustomerImeStock", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerImeStock(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model)  {
        String factoryAgentName="M13AMB";
        String localKey=MD5Utils.encode(factoryAgentName+dateStart+dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime= LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime=LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoPushSerivce.getOppoCustomerImeiStock(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerImeiStocks));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //店总数据上抛
    @RequestMapping(value ="pullCustomerSale", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerSale(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model) {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage = new OppoResponseMessage();
        if (!localKey.equals(key)) {
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        } else {
            LocalDate dateStartTime = LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime = LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerSale> oppoCustomerSales = oppoPushSerivce.getOppoCustomerSales(dateStartTime, dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSales));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店销售明细
    @RequestMapping(value ="pullCustomerSaleIme", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerSaleIme(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model)  {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime = LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime = LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerSaleImei> oppoCustomerSaleImes=oppoPushSerivce.getOppoCustomerSaleImes(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSaleImes));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店销售数据汇总
    @RequestMapping(value ="pullCustomerSaleCount", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerSaleCount(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model)  {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime = LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime = LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoPushSerivce.getOppoCustomerSaleCounts(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerSaleCounts));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //门店售后零售退货条码数据
    @RequestMapping(value ="pullCustomerAfterSaleIme", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerAfterSaleIme(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model)  {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime = LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime = LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoPushSerivce.getOppoCustomerAfterSaleImeis(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerAfterSaleImeis));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

    //演示机条码数据
    @RequestMapping(value ="pullCustomerDemoPhone", method = RequestMethod.GET)
    @ResponseBody
    public String pullCustomerDemoPhone(String key,String dateStart,String dateEnd,HttpServletResponse response, Model model)  {
        String factoryAgentName = "M13AMB";
        String localKey = MD5Utils.encode(factoryAgentName + dateStart + dateEnd);
        OppoResponseMessage responseMessage=new OppoResponseMessage();
        if(!localKey.equals(key)){
            responseMessage.setMessage("密钥不正确");
            responseMessage.setResult("false");
        }else{
            LocalDate dateStartTime = LocalDateUtils.parse(dateStart);
            LocalDate dateEndTime = LocalDateUtils.parse(dateEnd).plusDays(1);
            List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoPushSerivce.getOppoCustomerDemoPhone(dateStartTime,dateEndTime);
            responseMessage.setMessage(ObjectMapperUtils.writeValueAsString(oppoCustomerDemoPhones));
            responseMessage.setResult("success");
        }
        return ObjectMapperUtils.writeValueAsString(responseMessage);
    }

}

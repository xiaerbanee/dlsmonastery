package net.myspring.tool.modules.oppo.web.controller;

import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.oppo.domain.*;
import net.myspring.tool.modules.oppo.dto.OppoPlantSendImeiPpselDto;
import net.myspring.tool.modules.oppo.dto.OppoResponseMessage;
import net.myspring.tool.modules.oppo.service.OppoPullService;
import net.myspring.tool.modules.oppo.service.OppoPushSerivce;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OppoPullController {
    @Autowired
    private OppoPullService oppoPullService;
    @Autowired
    private CompanyConfigClient companyConfigClient;

    @RequestMapping(value = "pullFactoryData")
    public String pullFactoryData(String companyName,String date) {
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        String agentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String[] agentCodes=agentCode.split(CharConstant.COMMA);
        String passWord=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_PASSWORDS.name()).replace("\"","");
        String[] passWords=passWord.split(CharConstant.COMMA);
        //同步颜色编码
        List<OppoPlantProductSel> oppoPlantProductSels=oppoPullService.getOppoPlantProductSels(agentCodes[0],passWords[0]);
        oppoPullService.pullPlantProductSels(oppoPlantProductSels);
        //同步物料编码
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels=oppoPullService.getOppoPlantAgentProductSels(agentCodes[0],passWords[0]);
        oppoPullService.pullPlantAgentProductSels(oppoPlantAgentProductSels);
        //同步发货串码
        Map<String,List<OppoPlantSendImeiPpsel>> oppoPlantSendImeiPpselMap= Maps.newHashMap();
        for (int i = 0; i < agentCodes.length; i++) {
            oppoPlantSendImeiPpselMap.put(agentCodes[i],oppoPullService.getOppoPlantSendImeiPpsels(agentCodes[i],passWords[i],date)) ;
        }
        oppoPullService.pullPlantSendImeiPpsels(oppoPlantSendImeiPpselMap);
        //同步电子保卡
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels=oppoPullService.getOppoPlantProductItemelectronSels(agentCodes[0],passWords[0],date);
        oppoPullService.pullPlantProductItemelectronSels(oppoPlantProductItemelectronSels);
        return "OPPO同步成功";
    }


    @RequestMapping(value = "getSendImeList")
    public List<OppoPlantSendImeiPpselDto> getSendImeList(String date, String agentCode) {
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtos = oppoPullService.getSendImeList(date,agentCode);
        return oppoPlantSendImeiPpselDtos;
    }

    @RequestMapping(value = "getItemelectronSelList")
    public List<OppoPlantProductItemelectronSel> getItemelectronSelList(String date,String agentCode) {
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoPullService.getItemelectronSelList(date,agentCode);
        return oppoPlantProductItemelectronSels;
    }

}

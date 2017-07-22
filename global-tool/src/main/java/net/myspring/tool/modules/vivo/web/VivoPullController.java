package net.myspring.tool.modules.vivo.web;

import net.myspring.common.constant.CharConstant;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.dto.VivoPlantSendimeiDto;
import net.myspring.tool.modules.vivo.service.VivoPullService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "factory/vivo")
public class VivoPullController {
    @Autowired
    private VivoPullService vivoPullService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="pullFactoryData")
    public String pullFactoryData(String companyName,String date){
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        //同步物料编码
         List<VivoPlantProducts> vivoPlantProducts=vivoPullService.getPlantProducts(companyName);
        vivoPullService.pullPlantProducts(vivoPlantProducts,companyName);
        //同步发货串码
        List<VivoPlantSendimei> vivoPlantSendimeis=vivoPullService.getPlantSendimei(date);
        vivoPullService.pullPlantSendimeis(vivoPlantSendimeis);
        //同步电子保卡
        List<VivoPlantElectronicsn> vivoPlantElectronicsns = vivoPullService.getPlantElectronicsn(date);
        vivoPullService.pullPlantElectronicsns(vivoPlantElectronicsns);
        return "vivo同步成功";
    }

    @RequestMapping(value="getSendImeList")
    public List<VivoPlantSendimeiDto> getSendImeList (String companyName,String date, String agentCode){
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        List<VivoPlantSendimeiDto> vivoPlantSendimeiList=vivoPullService.getSendImeList(date,agentCode);
        return vivoPlantSendimeiList;
    }

    @RequestMapping(value = "getItemelectronSelList")
    public List<VivoPlantElectronicsn> getItemelectronSelList(String companyName,String date, String agentCode) {
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        List<VivoPlantElectronicsn> vivoPlantElectronicsnList = vivoPullService.getItemelectronSelList(date,agentCode);
        return vivoPlantElectronicsnList;
    }

    @RequestMapping(value="factoryOrder")
    public FactoryOrderDto factoryOrder(FactoryOrderDto factoryOrderDto){
        return vivoPullService.factoryOrder(factoryOrderDto);
    }

}

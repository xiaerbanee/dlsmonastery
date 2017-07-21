package net.myspring.tool.modules.vivo.web;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.dto.VivoPlantSendimeiDto;
import net.myspring.tool.modules.vivo.service.VivoPullService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "factory/vivo")
public class VivoPullController {
    @Autowired
    private VivoPullService vivoPullService;
    @Autowired
    private CompanyConfigClient companyConfigClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="pullFactoryData")
    public String pullFactoryData(String date){
        String agentCode="M13E00,M13D00,M13H00,M13I00,M13A00,M13C00,M13G00";
        List<String> agentCodes= Arrays.asList(agentCode.split(CharConstant.COMMA));
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        DbContextHolder.get().setCompanyName("JXVIVO");
        //同步颜色编码
        if(!"IDVIVO".equals(companyName)){
            List<VivoProducts> vivoProducts=vivoPullService.getProducts();
            vivoPullService.pullVivoProducts(vivoProducts);
        }
        //同步物料编码
         List<VivoPlantProducts> vivoPlantProducts=vivoPullService.getPlantProducts();
        logger.info("vivoPlantProducts=="+vivoPlantProducts.toString());
        vivoPullService.pullPlantProducts(vivoPlantProducts);
        //同步发货串码
        List<VivoPlantSendimei> vivoPlantSendimeis=vivoPullService.getPlantSendimei(date,agentCodes);
        vivoPullService.pullPlantSendimeis(vivoPlantSendimeis);
        //同步电子保卡
        List<VivoPlantElectronicsn> vivoPlantElectronicsns = vivoPullService.getPlantElectronicsn(date);
        vivoPullService.pullPlantElectronicsns(vivoPlantElectronicsns);
        return "vivo同步成功";
    }

    @RequestMapping(value="getSendImeList")
    public List<VivoPlantSendimeiDto> getSendImeList (String date, String agentCode){
        List<VivoPlantSendimeiDto> vivoPlantSendimeiList=vivoPullService.getSendImeList(date,agentCode);
        return vivoPlantSendimeiList;
    }

    @RequestMapping(value = "getItemelectronSelList")
    public List<VivoPlantElectronicsn> getItemelectronSelList(String date, String agentCode) {
        List<VivoPlantElectronicsn> vivoPlantElectronicsnList = vivoPullService.getItemelectronSelList(date,agentCode);
        return vivoPlantElectronicsnList;
    }

    @RequestMapping(value="factoryOrder")
    public FactoryOrderDto factoryOrder(FactoryOrderDto factoryOrderDto){
        return vivoPullService.factoryOrder(factoryOrderDto);
    }

}

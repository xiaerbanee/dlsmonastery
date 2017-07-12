package net.myspring.tool.modules.vivo.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.client.CompanyConfigClient;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.dto.FactoryOrderDto;
import net.myspring.tool.modules.vivo.service.VivoService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "factory/vivo")
public class VivoController {
    @Autowired
    private VivoService vivoService;
    @Autowired
    private RedisTemplate redisTemplate;
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
            List<VivoProducts> vivoProducts=vivoService.getProducts();
            logger.info("vivoProducts=="+vivoProducts.toString());
            vivoService.pullVivoProducts(vivoProducts);
        }
        //同步物料编码
        DbContextHolder.get().setCompanyName("JXVIVO");
        List<VivoPlantProducts> vivoPlantProducts=vivoService.getPlantProducts();
        logger.info("vivoPlantProducts=="+vivoPlantProducts.toString());
        vivoService.pullPlantProducts(vivoPlantProducts);
        //同步发货串码
        DbContextHolder.get().setCompanyName("JXVIVO");
        List<VivoPlantSendimei> vivoPlantSendimeis=vivoService.getPlantSendimei(date,agentCodes);
        vivoService.pullPlantSendimeis(vivoPlantSendimeis);

//        List<VivoProducts> vivoProductsList = vivoService.products();
//        List<VivoPlantProducts> vivoPlantProductsList = vivoService.plantProducts();
//        List<VivoPlantSendimei> vivoPlantSendimeis = vivoService.plantSendimei(localDate,agentCodes);
        //同步颜色编码
//        vivoService.pullProducts(vivoProductsList);
        //同步物料编码
//        vivoService.pullPlantProducts(vivoPlantProductsList);
//        //同步发货串吗
//        vivoService.pullPlantSendimeis(vivoPlantSendimeis);
//        //同步电子保卡
//        List<VivoPlantElectronicsn> vivoPlantElectronicsns = vivoService.plantElectronicsn(localDate);
//        vivoService.pullPlantElectronicsns(vivoPlantElectronicsns);
        return "vivo同步成功";
    }

    @RequestMapping(value="synIme")
    public  String synIme (String date){
        List<VivoPlantSendimei> vivoPlantSendimeiList=vivoService.synIme(date);
        return ObjectMapperUtils.writeValueAsString(vivoPlantSendimeiList);
    }

    @RequestMapping(value="factoryOrder")
    public FactoryOrderDto factoryOrder(FactoryOrderDto factoryOrderDto){
        return vivoService.factoryOrder(factoryOrderDto);
    }
}

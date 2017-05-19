package net.myspring.tool.modules.vivo.web;

import com.ctc.wstx.util.StringUtil;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei;
import net.myspring.tool.modules.vivo.domain.VivoProducts;
import net.myspring.tool.modules.vivo.service.VivoService;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "vivo")
public class VivoController {
    @Autowired
    private VivoService vivoService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value="syn")
    public String synFactoryVivo(String date){
        RequestUtils.getRequestEntity().setAccountId("1");
        RequestUtils.getRequestEntity().setCompanyId("1");
        List<String> agentCodes = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue(),CharConstant.COMMA);
        //同步颜色编码
        LocalDate localDate= LocalDateUtils.parse(date);
        List<VivoProducts> vivoProductsList = vivoService.products();
        List<VivoPlantProducts> vivoPlantProductsList = vivoService.plantProducts();
        List<VivoPlantSendimei> vivoPlantSendimeis = vivoService.plantSendimei(localDate,agentCodes);
        //同步颜色编码
        vivoService.pullProducts(vivoProductsList);
        //同步物料编码
        vivoService.pullPlantProducts(vivoPlantProductsList);
//        //同步发货串吗
        vivoService.pullPlantSendimeis(vivoPlantSendimeis);
//        //同步电子保卡
        List<VivoPlantElectronicsn> vivoPlantElectronicsns = vivoService.plantElectronicsn(localDate);
        vivoService.pullPlantElectronicsns(vivoPlantElectronicsns);
        RequestUtils.getRequestEntity().setAccountId("1");
        return "vivo同步成功";
    }
}

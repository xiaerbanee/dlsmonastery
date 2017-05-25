package net.myspring.tool.modules.oppo.web;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductItemelectronSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpsel;
import net.myspring.tool.modules.oppo.service.OppoService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;
    @Autowired
    private RedisTemplate redisTemplate;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="syn")
    public String synFactoryOppo(String date){
        List<String> mainCodes = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue(),CharConstant.COMMA);
        List<String> mainPasswords = StringUtils.getSplitList(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.FACTORY_AGENT_PASSWORDS.name()).getValue(),CharConstant.COMMA);
        LocalDate localDate = LocalDateUtils.parse(date);
        List<OppoPlantProductSel> plantProductSel = oppoService.plantProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        //同步颜色编码
        logger.info("开始同步颜色编码");
        oppoService.pullPlantProductSels(plantProductSel);
        logger.info("开始同步物料编码");
        //同步物料编码
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoService.plantAgentProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        String message=oppoService.pullPlantAgentProductSels(oppoPlantAgentProductSels);
        //同步发货串吗
        for (int i = 0; i < mainCodes.size(); i++) {
            List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselList = oppoService.plantSendImeiPPSel(mainCodes.get(i), mainPasswords.get(i),localDate);
            if (CollectionUtil.isNotEmpty(oppoPlantSendImeiPpselList)) {
                oppoService.pullPlantSendImeiPpsels(oppoPlantSendImeiPpselList, mainCodes.get(i));
            }
        }
        //同步电子保卡
        List<OppoPlantProductItemelectronSel> oppoPlantProductItemelectronSels = oppoService.plantProductItemelectronSel(mainCodes.get(0), mainPasswords.get(0),localDate);
        oppoService.pullPlantProductItemelectronSels(oppoPlantProductItemelectronSels);
        RequestUtils.getRequestEntity().setAccountId("1");
        return "OPPO同步成功";
    }

    @RequestMapping(value="synIme")
    public  String synIme (String date){
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpselDtos=oppoService.synIme(date);
        return ObjectMapperUtils.writeValueAsString(oppoPlantSendImeiPpselDtos);
    }
}

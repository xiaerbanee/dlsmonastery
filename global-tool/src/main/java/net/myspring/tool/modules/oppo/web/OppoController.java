package net.myspring.tool.modules.oppo.web;

import net.myspring.tool.common.utils.Const;
import net.myspring.tool.modules.oppo.domain.OppoPlantAgentProductSel;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import net.myspring.tool.modules.oppo.service.OppoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by guolm on 2017/5/9.
 */
@RestController
@RequestMapping(value = "oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="syn")
    public String synFactoryOppo(){
        List<String> mainCodes = Arrays.asList(oppoService.getCodes("FACTORY_AGENT_CODES").split(Const.CharEnum.COMMA.getValue()));
        List<String> mainPasswords = Arrays.asList(oppoService.getCodes("FACTORY_AGENT_PASSWORD").split(Const.CharEnum.COMMA.getValue()));
        List<OppoPlantProductSel> plantProductSel = oppoService.plantProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        for(OppoPlantProductSel oppoPlantProductSel:plantProductSel){
            oppoPlantProductSel.setColorId(oppoPlantProductSel.getColorId().trim());
        }
        //同步颜色编码
        logger.info("开始同步颜色编码");
        oppoService.pullPlantProductSels(plantProductSel);
        logger.info("开始同步物料编码");
        //同步物料编码
        List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoService.plantAgentProductSel(mainCodes.get(0), mainPasswords.get(0), "");
        String message=oppoService.pullPlantAgentProductSels(oppoPlantAgentProductSels);
        return message;
    }
}

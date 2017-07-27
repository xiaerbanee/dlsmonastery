package net.myspring.tool.modules.imoo.web;

import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.imoo.service.ImooPullService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by guolm on 2017/5/9.
 */
@RestController
@RequestMapping(value = "factory/imoo")
public class ImooPullController {
    @Autowired
    private ImooPullService imooPullService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="pullFactoryData")
    public String syn(String date){
        logger.info(LocalDateTime.now()+"imoo开始同步物料编码:");
        List<ImooPlantBasicProduct> imooPlantBasicProducts = imooPullService.imooPlantBasicProducts();
        imooPullService.pullPlantProducts(imooPlantBasicProducts);
        logger.info(LocalDateTime.now()+"imoo开始同步发货:");
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers = imooPullService.plantPrdocutImeiDeliverByDate(LocalDateUtils.parse(date));
        imooPullService.pullPlantSendimeis(imooPrdocutImeiDelivers);
        return "imoo同步成功";
    }

    @RequestMapping(value="synIme")
    public  String synIme (String date){
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers= imooPullService.synIme(date);
        return ObjectMapperUtils.writeValueAsString(imooPrdocutImeiDelivers);
    }
}

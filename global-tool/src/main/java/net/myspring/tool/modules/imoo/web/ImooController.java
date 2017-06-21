package net.myspring.tool.modules.imoo.web;

import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.imoo.service.ImooService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guolm on 2017/5/9.
        */
@RestController
@RequestMapping(value = "imoo")
public class ImooController {
    @Autowired
    private ImooService imooService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="syn")
    public String syn(String date){
        logger.info(LocalDateTime.now()+"imoo开始同步物料编码:");
        List<ImooPlantBasicProduct> imooPlantBasicProducts = imooService.imooPlantBasicProducts();
        imooService.pullPlantProducts(imooPlantBasicProducts);
        logger.info(LocalDateTime.now()+"imoo开始同步发货:");
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers = imooService.plantPrdocutImeiDeliverByDate(LocalDateUtils.parse(date));
        imooService.pullPlantSendimeis(imooPrdocutImeiDelivers);
        return "imoo同步成功";
    }

    @RequestMapping(value="synIme")
    public  String synIme (String date){
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers=imooService.synIme(date);
        return ObjectMapperUtils.writeValueAsString(imooPrdocutImeiDelivers);
    }
}

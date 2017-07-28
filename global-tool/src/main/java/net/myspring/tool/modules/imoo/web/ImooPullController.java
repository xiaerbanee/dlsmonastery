package net.myspring.tool.modules.imoo.web;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.tool.common.dataSource.DbContextHolder;
import net.myspring.tool.common.utils.RequestUtils;
import net.myspring.tool.modules.imoo.domain.ImooPlantBasicProduct;
import net.myspring.tool.modules.imoo.domain.ImooPrdocutImeiDeliver;
import net.myspring.tool.modules.imoo.dto.ImooProductDto;
import net.myspring.tool.modules.imoo.service.ImooPullService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public String pullFactoryData(String date){
        if (StringUtils.isBlank(RequestUtils.getCompanyName())){
            DbContextHolder.get().setCompanyName(CompanyNameEnum.JXDJ.name());
        }
        logger.info(LocalDateTime.now()+"imoo开始同步物料编码:");
        List<ImooPlantBasicProduct> imooPlantBasicProducts = imooPullService.imooPlantBasicProducts();
        imooPullService.pullPlantProducts(imooPlantBasicProducts);
        logger.info(LocalDateTime.now()+"imoo开始同步发货:");
        List<ImooPrdocutImeiDeliver> imooPrdocutImeiDelivers = imooPullService.plantPrdocutImeiDeliverByDate(LocalDateUtils.parse(date));
        imooPullService.pullPlantSendimeis(imooPrdocutImeiDelivers);
        return "imoo同步成功";
    }

    @RequestMapping(value = "getImooProductDtoMap")
    public Map<String, ImooProductDto> getImooProductDtoMap(){
        return imooPullService.getImooProductDtoMap();
    }

    @RequestMapping(value = "getSendImeList")
    public List<ImooPrdocutImeiDeliver> getSendImeList(LocalDate dateStart, LocalDate dateEnd, List<String> agentCodes){
        return imooPullService.getSendImeList(dateStart,dateEnd,agentCodes);
    }

}

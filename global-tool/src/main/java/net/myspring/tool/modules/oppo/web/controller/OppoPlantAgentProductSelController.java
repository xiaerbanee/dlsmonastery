package net.myspring.tool.modules.oppo.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.future.service.FutureProductService;
import net.myspring.tool.modules.oppo.dto.OppoPlantAgentProductSelDto;
import net.myspring.tool.modules.oppo.service.OppoPlantAgentProductSelService;
import net.myspring.tool.modules.oppo.web.query.OppoPlantAgentProductSelQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "factory/oppo/oppoPlantAgentProductSel")
public class OppoPlantAgentProductSelController {
    @Autowired
    private OppoPlantAgentProductSelService oppoPlantAgentProductSelService;
    @Autowired
    private FutureProductService futureProductService;

    @RequestMapping(value = "findAll")
    public List<OppoPlantAgentProductSelDto> findAll(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        List<OppoPlantAgentProductSelDto> oppoPlantAgentProductSelDtoList = oppoPlantAgentProductSelService.findAll(oppoPlantAgentProductSelQuery);
        return oppoPlantAgentProductSelDtoList;
    }

    @RequestMapping(value = "getQuery")
    public OppoPlantAgentProductSelQuery getQuery(OppoPlantAgentProductSelQuery oppoPlantAgentProductSelQuery){
        return oppoPlantAgentProductSelQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data){
        if(StringUtils.isNotBlank(data)){
            List<ProductDto> productDtoList = futureProductService.findHasImeProduct();
            oppoPlantAgentProductSelService.save(productDtoList,data);
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findByProductName")
    public List<ProductDto> findByProductName(String name){
        List<ProductDto> productDtoList = futureProductService.findByNameLike(name);
        return productDtoList;
    }

}
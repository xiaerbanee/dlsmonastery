package net.myspring.tool.modules.vivo.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.future.service.FutureProductService;
import net.myspring.tool.modules.vivo.dto.VivoPlantProductsDto;
import net.myspring.tool.modules.vivo.service.VivoPlantProductsService;
import net.myspring.tool.modules.vivo.web.query.VivoPlantProductsQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "factory/vivo/vivoPlantProducts")
public class VivoPlantProductsController {
    @Autowired
    private FutureProductService futureProductService;
    @Autowired
    private VivoPlantProductsService vivoPlantProductsService;

    @RequestMapping(value = "findAll")
    public List<VivoPlantProductsDto> findAll(VivoPlantProductsQuery vivoPlantProductsQuery){
        List<VivoPlantProductsDto> vivoPlantProductsDtoList = vivoPlantProductsService.findAll(vivoPlantProductsQuery);
        return vivoPlantProductsDtoList;
    }

    @RequestMapping(value = "getQuery")
    public VivoPlantProductsQuery getQuery(VivoPlantProductsQuery vivoPlantProductsQuery){
        return vivoPlantProductsQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data){
        if(StringUtils.isNotBlank(data)){
            List<ProductDto> productDtoList = futureProductService.findHasImeProduct();
            vivoPlantProductsService.save(productDtoList,data);
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

}
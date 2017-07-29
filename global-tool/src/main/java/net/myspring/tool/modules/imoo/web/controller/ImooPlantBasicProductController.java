package net.myspring.tool.modules.imoo.web.controller;

import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.future.service.FutureProductService;
import net.myspring.tool.modules.imoo.dto.ImooPlantBasicProductDto;
import net.myspring.tool.modules.imoo.service.ImooPlantBasicProductService;
import net.myspring.tool.modules.imoo.web.query.ImooPlantBasicProductQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "factory/imoo/imooPlantBasicProduct")
public class ImooPlantBasicProductController {
    @Autowired
    private ImooPlantBasicProductService imooPlantBasicProductService;
    @Autowired
    private FutureProductService futureProductService;

    @RequestMapping(value = "getQuery")
    public ImooPlantBasicProductQuery getQuery(ImooPlantBasicProductQuery imooPlantBasicProductQuery){
        return imooPlantBasicProductQuery;
    }

    @RequestMapping(value = "findAll")
    public List<ImooPlantBasicProductDto> findAll(ImooPlantBasicProductQuery imooPlantBasicProductQuery){
        return imooPlantBasicProductService.findAll(imooPlantBasicProductQuery);
    }

    @RequestMapping(value = "findByProductName")
    public List<ProductDto> findByProductName(String name){
        return futureProductService.findByNameLike(name);
    }

    @RequestMapping(value = "save")
    public RestResponse save(String data){
        if(StringUtils.isNotBlank(data)){
            List<ProductDto> productDtoList = futureProductService.findHasImeProduct();
            imooPlantBasicProductService.save(productDtoList,data);
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "export",method = RequestMethod.GET)
    public ModelAndView export(String date) throws IOException {

        return null;
    }

}

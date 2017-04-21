package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.Query.ChainQuery;
import net.myspring.future.modules.basic.web.Query.ProductTypeQuery;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductTypeDto> list(Pageable pageable, ProductTypeQuery productTypeQuery){
        Page<ProductTypeDto> page = productTypeService.findPage(pageable,productTypeQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ProductTypeForm productTypeForm) {
        productTypeService.logicDeleteOne(productTypeForm);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProductTypeForm productTypeForm) {
        productTypeService.save(productTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public ProductTypeForm findOne(ProductTypeForm productTypeForm){
        productTypeForm.setBoolMap(BoolEnum.getMap());
        return productTypeForm;
    }


    @RequestMapping(value="getListProperty")
    public ProductTypeQuery getListProperty(ProductTypeQuery productTypeQuery){
        productTypeQuery.setBoolMap(BoolEnum.getMap());
        return productTypeQuery;
    }

    @RequestMapping(value = "search")
    public List<ProductType> search(String name){
        List<ProductType> productTypeList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productTypeList = productTypeService.findByNameLike(name);
        }
        return productTypeList;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        return null;
    }

}

package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.enums.BoolEnum;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "basic/productType")
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

    @RequestMapping(value = "findForm")
    public ProductTypeForm findForm(ProductTypeForm productTypeForm){
        productTypeForm = productTypeService.findForm(productTypeForm);
        return productTypeForm;
    }


    @RequestMapping(value="getListProperty")
    public ProductTypeQuery getListProperty(ProductTypeQuery productTypeQuery){
        productTypeQuery.setBoolMap(BoolEnum.getMap());
        return productTypeQuery;
    }

    @RequestMapping(value = "searchById")
    public List<ProductTypeDto> searchById(String id){
        ProductType productType = productTypeService.findOne(id);
        List<ProductTypeDto> productTypeList = Lists.newArrayList();
        productTypeList.add(BeanUtil.map(productType, ProductTypeDto.class));
        return productTypeList;
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

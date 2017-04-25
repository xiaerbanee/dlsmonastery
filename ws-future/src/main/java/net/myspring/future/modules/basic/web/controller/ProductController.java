package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.Query.ChainQuery;
import net.myspring.future.modules.basic.web.Query.ProductQuery;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductDto> list(Pageable pageable, ProductQuery productQuery){
        Page<ProductDto> page = productService.findPage(pageable,productQuery);
        return page;
    }

    @RequestMapping(value = "filter")
    public List<ProductDto> filter(ProductQuery productQuery){
        List<ProductDto> productList = productService.findFilter(productQuery);
        return productList;
    }

    @RequestMapping(value = "getListProperty")
    public ProductQuery getListProperty(ProductQuery productQuery) {
        productQuery.setNetTypeList(NetTypeEnum.getList());
        productQuery.setOutGroupNameList(productService.findByOutName());
        productQuery.setBoolMap(BoolEnum.getMap());
        productQuery.setProductTypeList(productTypeService.findAll());
        return productQuery;
    }

    @RequestMapping(value = "findForm")
    public ProductForm findOne(ProductForm productForm){
        productForm=productService.findForm(productForm);
        productForm.setNetTypeList(NetTypeEnum.getList());
        return productForm;
    }

    @RequestMapping(value = "findHasImeProduct")
    public List<ProductDto> findHasImeProduct(){
        List<ProductDto> productList= productService.findHasImeProduct();
        return productList;
    }

    @RequestMapping(value = "searchAll")
    public List<ProductDto> searchAll(String name,String code){
        List<ProductDto> productList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productList = productService.findByNameLike(name);
        }else if(StringUtils.isNotBlank(code)){
            productList = productService.findByCodeLike(code);
        }
        return productList;
    }

    @RequestMapping(value = "search")
    public List<ProductDto> search(String name,String code){
        List<ProductDto> productList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productList = productService.findByNameLikeHasIme(name);
        }else if(StringUtils.isNotBlank(code)){
            productList = productService.findByCodeLikeHasIme(code);
        }
        return productList;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProductForm productForm) {
        productService.save(productForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "syn")
    public RestResponse syn() {
        productService.syn();
        return new RestResponse("同步成功",null);
    }
}

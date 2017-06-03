package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.service.ProductService;
import net.myspring.cloud.modules.sys.web.form.ProductForm;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by liuj on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "form")
    public ProductForm form (ProductForm productForm) {
        productForm = productService.getForm(productForm);
        return productForm;
    }

    @RequestMapping(value = "findByName")
    public ProductDto findByName(String name){
        ProductDto productDto = productService.findByName(name);
        return productDto;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProductForm productForm){
        productService.save(productForm);
        return new RestResponse("保存成功",null);
    }
}

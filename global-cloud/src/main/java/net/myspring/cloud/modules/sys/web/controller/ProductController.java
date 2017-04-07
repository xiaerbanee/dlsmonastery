package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.service.ProductService;
import net.myspring.util.json.ObjectMapperUtils;
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

    @RequestMapping(value = "findOne")
    public ProductDto findOne(String id) {
        ProductDto productDto=productService.findOne(id);
        return productDto;
    }

}

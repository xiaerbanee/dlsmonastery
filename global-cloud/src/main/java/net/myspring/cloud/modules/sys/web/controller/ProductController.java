package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.service.BdMaterialService;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.ProductDto;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.service.KingdeeBookService;
import net.myspring.cloud.modules.sys.service.ProductService;
import net.myspring.cloud.modules.sys.web.form.ProductForm;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Created by liuj on 2017/4/5.
 */
@RestController
@RequestMapping(value = "sys/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BdMaterialService bdMaterialService;
    @Autowired
    private KingdeeBookService kingdeeBookService;

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

    @RequestMapping(value = "findByCode")
    public ProductDto findByCode(String code){
        return productService.findByCode(code);
    }

    @RequestMapping(value = "syn")
    public RestResponse syn(ProductForm productForm){
        LocalDateTime maxDate = productService.findMaxOutDate();
        List<BdMaterial> bdMaterialList;
        if (maxDate != null){
            bdMaterialList = bdMaterialService.findByMaxModifyDate(maxDate);
        }else {
            bdMaterialList = bdMaterialService.findAll();
        }
        if (CollectionUtil.isNotEmpty(bdMaterialList)){
            KingdeeBook kingdeeBook = kingdeeBookService.findByAccountId(RequestUtils.getAccountId());
            productService.syn(bdMaterialList,kingdeeBook);
            return new RestResponse("同步成功",null);
        }
        return new RestResponse("未同步：金蝶找不到符合条件的货品",null);
    }
}

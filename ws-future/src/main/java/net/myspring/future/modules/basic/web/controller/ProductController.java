package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.form.ProductBatchForm;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "basic/product")
public class ProductController {

    @Autowired
    private ProductService productService;

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

    @RequestMapping(value = "searchFullText")
    public List<ProductDto> searchFullText(String key){
        if(StringUtils.isBlank(key)){
            return Lists.newArrayList();
        }
        List<ProductDto> list1 = productService.findByNameLike(key);
        List<ProductDto> list2 = productService.findByCodeLike(key);
        if(list1 == null){
            return list2;
        }

        if(list2 !=null){
            list1.addAll(list2);
        }
        return list1;
    }

    @RequestMapping(value = "searchById")
    public List<ProductDto> searchById(String id){
        ProductDto productDto = productService.findOne(id);
        List<ProductDto> productList = Lists.newArrayList();
        productList.add(productDto);
        return productList;
    }
    @RequestMapping(value = "findByIds")
    public List<ProductDto> findByListIds(@RequestParam("idStr") List<String> ids) {
        List<ProductDto> productDtoList =productService.findByIds(ids);
        return productDtoList;
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

    @RequestMapping(value = "batchSave")
    public RestResponse batchSave(ProductBatchForm productBatchForm){
        productService.batchSave(productBatchForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "syn")
    public RestResponse syn() {
        productService.syn();
        return new RestResponse("同步成功",null);
    }

    @RequestMapping(value = "findOne")
    public ProductDto detail(String id){
        return productService.findOne(id);
    }

    @RequestMapping(value="getQuery")
    public ProductQuery getQuery(ProductQuery productQuery){
        return productService.getQuery(productQuery);
    }

    @RequestMapping(value = "getForm")
    public ProductForm findOne(ProductForm productForm){
        productForm=productService.getForm(productForm);
        return productForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ProductDto productDto) {
        productService.delete(productDto);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }
}

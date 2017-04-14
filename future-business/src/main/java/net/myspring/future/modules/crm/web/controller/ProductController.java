package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.service.ProductService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ModelAttribute
    public Product get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Product() : productService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Product> page = productService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Product product: page.getContent()){
            product.setActionList(getActionList(product));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "filter")
    public String filter(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        List<Product> productList = productService.findFilter(searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(productList);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String,Object> searchPropertyMap = productService.getListProperty();
        return ObjectMapperUtils.writeValueAsString(searchPropertyMap);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("netTypes", NetTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findOne")
    public String findOne(Product product){
        return ObjectMapperUtils.writeValueAsString(product);
    }

    @RequestMapping(value = "findHasImeProduct")
    public String findHasImeProduct(){
        List<Product> productList= productService.findHasImeProduct();
        return ObjectMapperUtils.writeValueAsString(productList);
    }

    @RequestMapping(value = "searchAll")
    public String searchAll(String name,String code){
        List<Product> productList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productList = productService.findByNameLike(name);
        }else if(StringUtils.isNotBlank(code)){
            productList = productService.findByCodeLike(code);
        }
        return ObjectMapperUtils.writeValueAsString(productList);
    }

    @RequestMapping(value = "search")
    public String search(String name,String code){
        List<Product> productList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productList = productService.findByNameLikeHasIme(name);
        }else if(StringUtils.isNotBlank(code)){
            productList = productService.findByCodeLikeHasIme(code);
        }
        return ObjectMapperUtils.writeValueAsString(productList);
    }

    @RequestMapping(value = "save")
    public String save(Product product) {
        productService.save(product);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }


    @RequestMapping(value = "syn")
    public String syn() {
        productService.syn();
        return ObjectMapperUtils.writeValueAsString(new RestResponse("同步成功"));
    }

    private List<String> getActionList(Product product) {
        List<String> actionList = Lists.newArrayList();
        //门店才允许在手机端修改和终端统计
        if(SecurityUtils.getAuthorityList().contains("crm:product:edit")){
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        return actionList;
    }
}

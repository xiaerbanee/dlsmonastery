package net.myspring.future.modules.basic.web.controller;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.basic.service.ProductTypeService;
import net.myspring.future.modules.basic.web.form.ProductTypeForm;
import net.myspring.future.modules.basic.web.query.ProductTypeQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "basic/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductTypeDto> list(Pageable pageable, ProductTypeQuery productTypeQuery){
        return productTypeService.findPage(pageable,productTypeQuery);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        productTypeService.logicDelete(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProductTypeForm productTypeForm) {
        productTypeService.save(productTypeForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="getQuery")
    public ProductTypeQuery getQuery(ProductTypeQuery productTypeQuery){
        return productTypeQuery;
    }

    @RequestMapping(value = "searchByIds")
    public List<ProductTypeDto> searchById(String id){
        List<String> ids= StringUtils.getSplitList(id, CharConstant.COMMA);
        List<ProductTypeDto> productTypeList = productTypeService.findByIds(ids);
        return productTypeList;
    }

    @RequestMapping(value = "findDto")
    public ProductTypeDto findDto(String id){
        if(StringUtils.isBlank(id)){
            return new ProductTypeDto();
        }
        return productTypeService.findDto(id);

    }

    @RequestMapping(value = "search")
    public List<ProductTypeDto> search(String name){
        return productTypeService.findByNameLike(name);
    }

    @RequestMapping(value = "export")
    public String export(ProductTypeQuery productTypeQuery) {

        return productTypeService.export(productTypeQuery);
    }

    @RequestMapping(value = "getForm")
    public ProductTypeForm getForm(ProductTypeForm productTypeForm) {
        return productTypeForm;
    }

}

package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.service.ProductImeSaleService;
import net.myspring.future.modules.crm.service.StoreAllotDetailService;
import net.myspring.future.modules.crm.web.form.ProductImeSaleBackForm;
import net.myspring.future.modules.crm.web.form.ProductImeSaleForm;
import net.myspring.future.modules.crm.web.query.ProductImeSaleQuery;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/productImeSale")
public class ProductImeSaleController {

    @Autowired
    private ProductImeSaleService productImeSaleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductImeSaleDto> list(Pageable pageable, ProductImeSaleQuery productImeSaleQuery){
        return productImeSaleService.findPage(pageable, productImeSaleQuery);
    }

    @RequestMapping(value = "getQuery")
    public ProductImeSaleQuery getQuery(ProductImeSaleQuery productImeSaleQuery) {
        return productImeSaleQuery;
    }

    @RequestMapping(value = "findDto")
    public ProductImeSaleDto findDto(String id) {
        ProductImeSaleDto result = productImeSaleService.findDto(id);
        if(result == null){
            result = new ProductImeSaleDto();
        }
        return result;
    }

    @RequestMapping(value = "checkForSale")
    public String checkForSale(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return productImeSaleService.checkForSale(imeList);
    }

    @RequestMapping(value = "checkForSaleBack")
    public String checkForSaleBack(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return productImeSaleService.checkForSaleBack(imeList);
    }


    @RequestMapping(value = "getForm")
    public String getForm(ProductImeSale productImeSale) {
        return null;
    }

    @RequestMapping(value = "sale")
    public RestResponse sale(ProductImeSaleForm productImeSaleForm) {

        List<String> imeList = productImeSaleForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            return new RestResponse("没有输入任何有效的IME", ResponseCodeEnum.invalid.name(), false);
        }

        String errMsg = productImeSaleService.checkForSale(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse(errMsg, ResponseCodeEnum.invalid.name(), false);
        }

        productImeSaleService.sale(productImeSaleForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "saleBack")
    public RestResponse saleBack(ProductImeSaleBackForm productImeSaleBackForm) {
        List<String> imeList = productImeSaleBackForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            return new RestResponse("没有输入任何有效的IME", ResponseCodeEnum.invalid.name(), false);
        }

        String errMsg = productImeSaleService.checkForSaleBack(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse(errMsg, ResponseCodeEnum.invalid.name(), false);
        }

        productImeSaleService.saleBack(productImeSaleBackForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value="export")
    public String export(ProductImeSaleQuery productImeSaleQuery) {

        return productImeSaleService.export(productImeSaleQuery);
    }

}

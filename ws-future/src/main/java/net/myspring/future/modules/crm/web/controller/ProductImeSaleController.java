package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.dto.ProductImeForSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.service.ProductImeSaleService;
import net.myspring.future.modules.crm.web.form.ProductImeSaleBackForm;
import net.myspring.future.modules.crm.web.form.ProductImeSaleForm;
import net.myspring.future.modules.crm.web.query.ProductImeSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productImeSale")
public class ProductImeSaleController {

    @Autowired
    private ProductImeSaleService productImeSaleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductImeSaleDto> list(Pageable pageable, ProductImeSaleQuery productImeSaleQuery){
        if(StringUtils.isNotBlank(productImeSaleQuery.getIme())&&productImeSaleQuery.getIme().length()<6){
            throw new ServiceException("请输入至少六位串码尾数");
        }
        return productImeSaleService.findPage(pageable, productImeSaleQuery);
    }

    @RequestMapping(value = "getQuery")
    public ProductImeSaleQuery getQuery(ProductImeSaleQuery productImeSaleQuery) {
        return productImeSaleQuery;
    }

    @RequestMapping(value = "findDto")
    public ProductImeSaleDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ProductImeSaleDto();
        }
        return productImeSaleService.findDto(id);
    }

    @RequestMapping(value = "checkForSale")
    public String checkForSale(String imeStr) {
        return null;
    }

    @RequestMapping(value = "checkForSaleBack")
    public String checkForSaleBack(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return productImeSaleService.checkForSaleBack(imeList);
    }

    @RequestMapping(value="findProductImeForSaleDto")
    public List<ProductImeForSaleDto> findProductImeForSaleDto(String imeStr) {
        return productImeSaleService.findProductImeForSaleDto(imeStr);
    }

    @RequestMapping(value = "sale")
    public RestResponse sale(ProductImeSaleForm productImeSaleForm) {
        List<String> imeList = productImeSaleForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            throw new ServiceException("没有输入任何有效的串码");
        }

        productImeSaleService.sale(productImeSaleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "saleBack")
    public RestResponse saleBack(ProductImeSaleBackForm productImeSaleBackForm) {
        List<String> imeList = productImeSaleBackForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            throw new ServiceException("没有输入任何有效的串码");
        }

        String errMsg = productImeSaleService.checkForSaleBack(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            throw new ServiceException(errMsg);
        }

        productImeSaleService.saleBack(productImeSaleBackForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="export")
    public ModelAndView export(ProductImeSaleQuery productImeSaleQuery) {
        if(StringUtils.isNotBlank(productImeSaleQuery.getIme())&&productImeSaleQuery.getIme().length()<6){
            throw new ServiceException("请输入至少六位串码尾数");
        }
        return new ModelAndView(new ExcelView(), "simpleExcelBook", productImeSaleService.export(productImeSaleQuery));
    }

    @RequestMapping(value="getSaleForm")
    public  ProductImeSaleForm getSaleForm(ProductImeSaleForm productImeSaleForm) {
        return productImeSaleForm;
    }

    @RequestMapping(value="getSaleBackForm")
    public  ProductImeSaleBackForm getSaleBackForm(ProductImeSaleBackForm productImeSaleBackForm) {
        return productImeSaleBackForm;
    }
}

package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDetailDto;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDto;
import net.myspring.future.modules.crm.service.ProductMonthPriceService;
import net.myspring.future.modules.crm.web.form.ProductMonthPriceForm;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productMonthPrice")
public class ProductMonthPriceController {

    @Autowired
    private ProductMonthPriceService productMonthPriceService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductMonthPriceDto> list(Pageable pageable, ProductMonthPriceQuery ProductMonthPriceQuery){
        return productMonthPriceService.findPage(pageable,ProductMonthPriceQuery);
    }

    @RequestMapping(value = "getQuery")
    public ProductMonthPriceQuery getQuery(ProductMonthPriceQuery productMonthPriceQuery) {
        return productMonthPriceQuery;
    }

    @RequestMapping(value = "getForm")
    public ProductMonthPriceForm getForm(ProductMonthPriceForm productMonthPriceForm) {
        return productMonthPriceForm;
    }
    @RequestMapping(value = "findDto")
    public ProductMonthPriceDto findDto(String id){

        if(StringUtils.isBlank(id)){
            return new ProductMonthPriceDto();
        }

        return productMonthPriceService.findDto(id);
    }

    @RequestMapping(value = "findDetailListForNew")
    public List<ProductMonthPriceDetailDto> findDetailListForNew() {

        return productMonthPriceService.findDetailListForNew();

    }

    @RequestMapping(value = "findDetailListForEdit")
    public List<ProductMonthPriceDetailDto> findDetailListForEdit(String productMonthPriceId) {

        return productMonthPriceService.findDetailListForEdit(productMonthPriceId);

    }


    @RequestMapping(value = "checkMonth")
    public String checkMonth(String id, String month) {
        return productMonthPriceService.checkMonth(id, month);
    }

    @RequestMapping(value = "save")
    public RestResponse save(ProductMonthPriceForm productMonthPriceForm) {
        productMonthPriceService.save(productMonthPriceForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

}

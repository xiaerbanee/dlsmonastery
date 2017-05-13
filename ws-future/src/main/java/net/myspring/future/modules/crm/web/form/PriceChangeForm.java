package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.ProductTypeDto;
import net.myspring.future.modules.crm.domain.PriceChange;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/12.
 */
public class PriceChangeForm extends DataForm<PriceChange>{

    private String name;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;

    private List<ProductTypeDto> allProductTypeDtos;

    private List<ProductTypeDto> productTypeDtos;

    private List<String> productTypeIdList;

    public List<String> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeIdList(List<String> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPriceChangeDate() {
        return priceChangeDate;
    }

    public void setPriceChangeDate(LocalDate priceChangeDate) {
        this.priceChangeDate = priceChangeDate;
    }

    public LocalDate getUploadEndDate() {
        return uploadEndDate;
    }

    public void setUploadEndDate(LocalDate uploadEndDate) {
        this.uploadEndDate = uploadEndDate;
    }

    public List<ProductTypeDto> getAllProductTypeDtos() {
        return allProductTypeDtos;
    }

    public void setAllProductTypeDtos(List<ProductTypeDto> allProductTypeDtos) {
        this.allProductTypeDtos = allProductTypeDtos;
    }

    public List<ProductTypeDto> getProductTypeDtos() {
        return productTypeDtos;
    }

    public void setProductTypeDtos(List<ProductTypeDto> productTypeDtos) {
        this.productTypeDtos = productTypeDtos;
    }
}

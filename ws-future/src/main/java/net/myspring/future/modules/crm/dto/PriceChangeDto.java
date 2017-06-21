package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.PriceChange;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/12.
 */
public class PriceChangeDto extends DataDto<PriceChange> {

    private String name;
    private String productTypeName;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;
    private Integer checkPercent;
    private String status;

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

    public Integer getCheckPercent() {
        return checkPercent;
    }

    public void setCheckPercent(Integer checkPercent) {
        this.checkPercent = checkPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}

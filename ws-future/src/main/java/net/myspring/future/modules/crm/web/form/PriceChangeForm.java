package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.PriceChange;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/12.
 */
public class PriceChangeForm extends BaseForm<PriceChange> {

    private String name;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;
    private List<String> productTypeIdList;
    private String productTypeNames;
    private Integer checkPercent;

    public String getProductTypeNames() {
        return productTypeNames;
    }

    public void setProductTypeNames(String productTypeNames) {
        this.productTypeNames = productTypeNames;
    }

    public Integer getCheckPercent() {
        return checkPercent;
    }

    public void setCheckPercent(Integer checkPercent) {
        this.checkPercent = checkPercent;
    }

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

}

package net.myspring.future.modules.crm.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/12.
 */
public class PriceChangeDto extends DataDto<PriceChange> {

    private String name;
    private LocalDate priceChangeDate;
    private LocalDate uploadEndDate;
    private Integer checkPercent;
    private String status;

    private String productTypeIds;
    private List<String> productTypeIdList;
    @CacheInput(inputKey = "productTypes",inputInstance = "productTypeIdList",outputInstance = "name")
    private List<String> productTypeNameList;

    public String getProductTypeName(){
        String productTypeNames = "";
        if(this.productTypeNameList != null){
            for (String productTypeName:this.productTypeNameList){
                productTypeNames += productTypeName;
            }
        }
        return productTypeNames;
    }

    public List<String> getProductTypeNameList() {
        return productTypeNameList;
    }

    public void setProductTypeNameList(List<String> productTypeNameList) {
        this.productTypeNameList = productTypeNameList;
    }

    public String getProductTypeIds() {
        return productTypeIds;
    }

    public void setProductTypeIds(String productTypeIds) {
        this.productTypeIds = productTypeIds;
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
}

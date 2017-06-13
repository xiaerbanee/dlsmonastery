package net.myspring.future.modules.crm.dto;

import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/6/7.
 */
public class ProductImeReportDto {
    private String id;
    private String officeId;
    private String productTypeId;
    private Integer qty;
    private String percent;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String productTypeName;

    public ProductImeReportDto(){}

    public ProductImeReportDto(String officeId, Integer qty){
        this.officeId=officeId;
        this.qty=qty;
    }

        public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public void addQty(Integer addQty){
        if(this.qty!=null){
            this.qty+=addQty;
        }else {
            this.qty=addQty;
        }
    }
}

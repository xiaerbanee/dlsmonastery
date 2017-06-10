package net.myspring.future.modules.basic.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class ProductTypeDto extends DataDto<ProductType> {

    private String name;
    private String reportName;
    private String code;
    private String productNames;
    private Boolean locked;
    private Boolean enabled;
    private BigDecimal baokaPrice;
    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal price3;
    private Boolean scoreType;
    private String productIds;

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public String getScoreTypeName(){
        if(scoreType==null){
            return "";
        }else if(scoreType){
            return "是";
        }else{
            return "否";
        }

    }
    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
        this.scoreType = scoreType;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getProductIdList(){
        return StringUtils.getSplitList(productIds, CharConstant.COMMA);
    }
}
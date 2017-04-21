package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.ProductType;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/17.
 */
public class ProductTypeDto extends DataDto<ProductType> {
    private String name;
    private String reportName;
    private String code;
    private String productNames;
    private BigDecimal price1;
    private Boolean scoreType;
    private Boolean locked;
    private Boolean enabled;

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
}
package net.myspring.future.modules.basic.domain;


import com.google.common.collect.Lists;
import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.future.common.domain.DataEntity;
import net.myspring.future.modules.basic.web.form.ProductForm;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="crm_product_type")
public class ProductType extends CompanyEntity<ProductType> {
    private String name;
    private String reportName;
    private String code;
    private Integer version = 0;
    private BigDecimal baokaPrice;
    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal price3;
    private Boolean scoreType;
    private String demoPhoneTypeId;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
        this.scoreType = scoreType;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }
}

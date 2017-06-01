package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductTypeForm extends BaseForm<ProductType> {
    private String name;
    private String reportName;
    private Boolean scoreType;
    private String code;
    private BigDecimal baokaPrice;
    private List<String> productIdList;

    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal price3;

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

    public Boolean getScoreType() {
        return scoreType;
    }

    public void setScoreType(Boolean scoreType) {
        this.scoreType = scoreType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public List<String> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<String> productIdList) {
        this.productIdList = productIdList;
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
}

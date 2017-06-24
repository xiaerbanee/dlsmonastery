package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Product;

import java.math.BigDecimal;

/**
 * Created by zhangyf on 2017/6/24.
 */
public class ProductAdApplyDto extends DataDto<Product>{
    private String name;
    private String code;
    private String expiryDateRemarks;
    private BigDecimal price2;
    private Integer applyQty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpiryDateRemarks() {
        return expiryDateRemarks;
    }

    public void setExpiryDateRemarks(String expiryDateRemarks) {
        this.expiryDateRemarks = expiryDateRemarks;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public Integer getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(Integer applyQty) {
        this.applyQty = applyQty;
    }
}

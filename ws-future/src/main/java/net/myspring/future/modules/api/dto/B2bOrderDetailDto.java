package net.myspring.future.modules.api.dto;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by 23950 on 2016/11/25.
 */
public class B2bOrderDetailDto {

    private String mallProductId;
    private String mallProductType;
    private Integer qty;
    private List<String> imes= Lists.newArrayList();

    private String mallProductTypeAndColor;

    public String getMallProductId() {
        return mallProductId;
    }

    public void setMallProductId(String mallProductId) {
        this.mallProductId = mallProductId;
    }

    public String getMallProductType() {
        return mallProductType;
    }

    public void setMallProductType(String mallProductType) {
        this.mallProductType = mallProductType;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public List<String> getImes() {
        return imes;
    }

    public void setImes(List<String> imes) {
        this.imes = imes;
    }

    public String getMallProductTypeAndColor() {
        return mallProductTypeAndColor;
    }

    public void setMallProductTypeAndColor(String mallProductTypeAndColor) {
        this.mallProductTypeAndColor = mallProductTypeAndColor;
    }
}

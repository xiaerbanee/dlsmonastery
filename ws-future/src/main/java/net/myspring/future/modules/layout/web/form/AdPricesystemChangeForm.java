package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.web.form.AdPricesystemDetailForm;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeForm extends BaseForm<AdPricesystemChange> {

    private String productId;
    private String productName;
    private String productCode;
    private BigDecimal volume;
    private BigDecimal shouldGet;
    private List<AdPricesystemDetailForm> adPricesystemDetailFormList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getShouldGet() {
        return shouldGet;
    }

    public void setShouldGet(BigDecimal shouldGet) {
        this.shouldGet = shouldGet;
    }

    public List<AdPricesystemDetailForm> getAdPricesystemDetailFormList() {
        return adPricesystemDetailFormList;
    }

    public void setAdPricesystemDetailFormList(List<AdPricesystemDetailForm> adPricesystemDetailFormList) {
        this.adPricesystemDetailFormList = adPricesystemDetailFormList;
    }
}

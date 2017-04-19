package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.ProductType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="crm_price_change_commission")
public class PriceChangeCommission extends CompanyEntity<PriceChangeCommission> {
    private String areaId;
    private BigDecimal amount;
    private Integer version = 0;
    private PriceChange priceChange;
    private String priceChangeId;
    private String positionId;
    private ProductType productType;
    private String productTypeId;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public PriceChange getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(PriceChange priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangeId() {
        return priceChangeId;
    }

    public void setPriceChangeId(String priceChangeId) {
        this.priceChangeId = priceChangeId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }
}

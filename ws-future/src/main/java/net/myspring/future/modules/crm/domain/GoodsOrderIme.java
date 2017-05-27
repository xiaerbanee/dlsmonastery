package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.future.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_goods_order_ime")
public class GoodsOrderIme extends CompanyEntity<GoodsOrderIme> {
    private Integer version = 0;
    private Boolean mallStatus;
    private String goodsOrderId;
    private String productImeId;
    private String productId;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getMallStatus() {
        return mallStatus;
    }

    public void setMallStatus(Boolean mallStatus) {
        this.mallStatus = mallStatus;
    }

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}

package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_stock_check_detail")
public class StockCheckDetail extends IdEntity<StockCheckDetail> {
    private String type;
    private ProductIme productIme;
    private String productImeId;
    private StockCheck stockCheck;
    private String stockCheckId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProductIme getProductIme() {
        return productIme;
    }

    public void setProductIme(ProductIme productIme) {
        this.productIme = productIme;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public StockCheck getStockCheck() {
        return stockCheck;
    }

    public void setStockCheck(StockCheck stockCheck) {
        this.stockCheck = stockCheck;
    }

    public String getStockCheckId() {
        return stockCheckId;
    }

    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
}
